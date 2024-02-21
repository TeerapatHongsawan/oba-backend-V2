library 'lib-deploy-helm@v1.10.1'
deployhelm(
        myenvironment: 'dev',
        agent_label: 'k8s-eks-nonprod',
        image_name: 'ap2131-reportengine/oba-backend-core-ms',
        harbor_url: 'https://harbordev.se.scb.co.th',
        harbor_creds_id: 'my_app_harbor_credential_id',
        eks_cluster_name: 'SCB-SIT-EKS' ,
        iam_role_arn: 'arn:aws:iam::652707267819:role/myappRole',
        namespace: 'mynamespace',
        release_name: 'myservice',
        chart_repo: 'scbharborlibrary',
        chart_name: 'scb-common-deployment',
        chart_version: 'v1.7.0',
        value_file_path: 'oba-svc-backend-core-ms/values-dev.yaml',
        // aws_profile: 'my_aws_profile_name',
        // enable_uninstall: false,
        // git_params: 'PT_BRANCH',
        // helm_binary_name: 'helm3.8',
        // helm_timeout: '600s',
        email_receiver: 's97012@scb.co.th'
)