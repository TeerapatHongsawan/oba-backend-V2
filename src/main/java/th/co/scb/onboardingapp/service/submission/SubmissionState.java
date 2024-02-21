package th.co.scb.onboardingapp.service.submission;

import com.google.common.base.Throwables;
import com.spotify.futures.CompletableFutures;
import lombok.extern.slf4j.Slf4j;
import th.co.scb.onboardingapp.model.TJLogActivity;
import io.atlassian.fugue.Pair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class SubmissionState {
    private String caseId;
    private String pluginName;
    private boolean isHardStop;
    private Collection<TJLogActivity> tjlogList;
    private List<CompletableFuture<Runnable>> tasks = new ArrayList<>();
    private List<Pair<Consumer<Throwable>, String>> rollbackTasks = new ArrayList<>();

    public SubmissionState(String caseId, String pluginName, Collection<TJLogActivity> tjlogList, boolean isHardStop) {
        this.caseId = caseId;
        this.pluginName = pluginName;
        this.tjlogList = tjlogList;
        this.isHardStop = isHardStop;
    }

    public <T> void submit(CompletableFuture<T> future) {
        submit(future, null);
    }

    public <T> void submit(CompletableFuture<T> future, Consumer<T> accept) {
        submit(future, accept, null);
    }

    public <T> void submit(CompletableFuture<T> future, Consumer<T> accept, Consumer<Throwable> acceptEx) {
        if (accept == null) {
            tasks.add(future.thenApply(any -> () -> {
            }));
        } else {
            if (acceptEx == null) {
                tasks.add(future.thenApply(result -> () -> accept.accept(result)));
            } else {
                tasks.add(future.handle((result, th) -> {
                    if (th == null) {
                        return () -> accept.accept(result);
                    } else {
                        acceptEx.accept(th);
                        throw Throwables.propagate(th);
                    }
                }));
            }
        }
    }

    public void rollbackIfFailed(Consumer<Throwable> rollback, String accountNumber) {
        rollbackTasks.add(Pair.pair(rollback, accountNumber));
    }

    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    public CompletableFuture<SubmissionResult> getTasks() {
        LocalDateTime start = LocalDateTime.now();
        return CompletableFutures.allAsList(tasks)
                .handle((list, exception) -> {
                    if (exception == null) {
                        for (Runnable runnable : list) {
                            try {
                                runnable.run();
                            } catch (Exception ex) {
                                if (exception == null) {
                                    exception = ex;
                                } else {
                                    exception.addSuppressed(ex);
                                }
                            }
                        }
                    }
                    long time = System.currentTimeMillis() - start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    WorkflowStep step;
                    if (exception != null) {
                        log.error("Error in SubmissionState.getTasks", exception);
                        step = new WorkflowStep(pluginName, WorkflowStep.ERROR, exception.getMessage(), isHardStop, start, time, null);
                    } else {
                        log.info("getTasks: {} -> size: {}, caseId: {}, time: {}", pluginName, tasks.size(), caseId, time);
                        step = new WorkflowStep(pluginName, WorkflowStep.SUCCESS, null, isHardStop, start, time, null);
                    }
                    SubmissionResult result = new SubmissionResult();
                    result.setState(this);
                    result.setStep(step);
                    result.setEx((Exception) exception);
                    return result;
                });
    }

    public boolean hasRollbackTasks() {
        return !rollbackTasks.isEmpty();
    }

    public Function<Throwable, WorkflowStep> getRollbackTasks() {
        LocalDateTime start = LocalDateTime.now();
        return ex -> {
            List<String> rollbackStepExMsgs = new ArrayList<>();
            List<String> failedAccountNumbers = new ArrayList<>();
            List<Boolean> success=new ArrayList<>();
            for (Pair<Consumer<Throwable>, String> pair : rollbackTasks) {
                failedAccountNumbers.add(pair.right());
                try {
                    pair.left().accept(ex);
                    success.add(true);
                } catch (Exception rollbackStepEx) {
                    rollbackStepExMsgs.add(rollbackStepEx.getMessage());
                }

            }
            long time = System.currentTimeMillis() - start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            log.info("getRollbackTasks: {} -> size: {}, caseId: {}, time: {}", pluginName, rollbackTasks.size(), caseId, time);

            if (success.size()>0) {
                return new WorkflowStep("-" + this.pluginName, WorkflowStep.ROLLBACK_SUCCESS, null, isHardStop, start, time, failedAccountNumbers);
            } else {
                return new WorkflowStep("-" + this.pluginName, WorkflowStep.ROLLBACK_ERROR, String.join("\n - ", rollbackStepExMsgs), isHardStop, start, time, failedAccountNumbers);
            }
        };
    }

    public Collection<TJLogActivity> getTjlogList() {
        return tjlogList;
    }
}
