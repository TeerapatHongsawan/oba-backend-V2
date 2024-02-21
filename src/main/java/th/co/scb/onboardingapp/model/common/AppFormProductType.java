package th.co.scb.onboardingapp.model.common;

import java.util.EnumSet;

import static th.co.scb.onboardingapp.model.common.AppFormTypeEnum.*;


public enum AppFormProductType {


    DEPOSIT(EnumSet.of(S002, P008_1, P008_2, P006, P009, P022, P023)) {
        @Override
        public String toString() {
            return "001";
        }
    },
    MUTUALFUND(EnumSet.of(S003, P012, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "002";
        }
    },
    SECURITIES(EnumSet.of(S006, P008_1, P008_2, P022)) {
        @Override
        public String toString() {
            return "003";
        }
    },
    SERVICE_ONLY(EnumSet.of(S304, P008_1, P008_2, P022)) {
        @Override
        public String toString() {
            return "004";
        }
    },
    MUTUALFUND_SECURITIES(EnumSet.of(S003, S009, P008_1, P008_2, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "005";
        }
    },
    DEPOSIT_MUTUALFUND(EnumSet.of(S010, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "006";
        }
    },
    SECURITIES_SERVICES(EnumSet.of(S005, P008_1, P008_2, P022)) {
        @Override
        public String toString() {
            return "007";
        }
    },
    DEPOSIT_SECURITIES(EnumSet.of(S013, S009, P008_1, P008_2, P006, P022, P023)) {
        @Override
        public String toString() {
            return "008";
        }
    },
    MUTUALFUND_SERVICES(EnumSet.of(S008, P008_1, P008_2, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "009";
        }
    },
    MUTUALFUND_SECURITIES_SERVICES(EnumSet.of(S008, S009, P008_1, P008_2, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "010";
        }
    },
    DEPOSIT_MUTUALFUND_SECURITIES(EnumSet.of(S014, S009, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "011";
        }
    },
    OMNIBUS(EnumSet.of(S303, P304, P008_1, P008_2, P022)) {
        @Override
        public String toString() {
            return "012";
        }
    },
    DEPOSIT_OMNIBUS(EnumSet.of(S002, S303_1, P304, P008_1, P008_2, P022, P023)) {
        @Override
        public String toString() {
            return "013";
        }
    },
    MUTUALFUND_OMNIBUS(EnumSet.of(S003, S303_1, P012, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "014";
        }
    },
    OMNIBUS_SECURITIES(EnumSet.of(S303, S009, P008_1, P008_2, P022, P304, P023)) {
        @Override
        public String toString() {
            return "015";
        }
    },
    DEPOSIT_MUTUALFUND_OMNIBUS(EnumSet.of(S010, S303_1, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "016";
        }
    },
    DEPOSIT_OMNIBUS_SECURITIES(EnumSet.of(S002, S303_1, S009, P304, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "017";
        }
    },
    MUTUALFUND_OMNIBUS_SECURITIES(EnumSet.of(S003, S303_1, S009, P012, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "018";
        }
    },
    DEPOSIT_MUTUALFUND_OMNIBUS_SECURITIES(EnumSet.of(S010, S303_1, S009, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "019";
        }
    },
    OMNIBUS_SECURITIES_SERVICES(EnumSet.of(S306, S009, P008_1, P008_2, P022, P304, P023)) {
        @Override
        public String toString() {
            return "020";
        }
    },
    MUTUALFUND_OMNIBUS_SERVICES(EnumSet.of(S008, S303_1, P012, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "021";
        }
    },
    MUTUALFUND_OMNIBUS_SECURITIES_SERVICES(EnumSet.of(S008, S303_1, S009, P012, P008_1, P008_2, P009, P022, P023)) {
        @Override
        public String toString() {
            return "022";
        }
    },
    DEPOSIT_OMNIBUS_SERVICES(EnumSet.of(S002, S303_1, P304, P008_1, P008_2, P022, P023)) {
        @Override
        public String toString() {
            return "023";
        }
    },
    DEPOSIT_MUTUALFUND_OMNIBUS_SECURITIES_SERVICES(EnumSet.of(S010, S303_1, S009, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "024";
        }
    },
    DEPOSIT_MUTUALFUND_OMNIBUS_SERVICES(EnumSet.of(S010, S303_1, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "025";
        }
    },
    OMNIBUS_SERVICES(EnumSet.of(S306, P304, P008_1, P008_2, P022)) {
        @Override
        public String toString() {
            return "026";
        }
    },
    DEPOSIT_OMNIBUS_SECURITIES_SERVICES(EnumSet.of(S002, S303_1, S009, P304, P008_1, P008_2, P022, P023)) {
        @Override
        public String toString() {
            return "027";
        }
    },
    DEPOSIT_SERVICES(EnumSet.of(S002, P008_1, P008_2, P006, P022, P023)) {
        @Override
        public String toString() {
            return "028";
        }
    },
    DEPOSIT_MUTUALFUND_SERVICES(EnumSet.of(S010, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "029";
        }
    },
    DEPOSIT_SECURITIES_SERVICES(EnumSet.of(S013, S009, P008_1, P008_2, P006, P022, P023)) {
        @Override
        public String toString() {
            return "030";
        }
    },
    DEPOSIT_MUTUALFUND_SECURITIES_SERVICES(EnumSet.of(S014, S009, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "031";
        }
    },
    DEPOSIT_CHAIYO(EnumSet.of(S016, P008_1, P008_2, P022, P023)) {
        @Override
        public String toString() {
            return "032";
        }
    },
    DEPOSIT_SERVICE_CHAIYO(EnumSet.of(S014, S009, P008_1, P008_2, P006, P009, P022, P012, P023)) {
        @Override
        public String toString() {
            return "033";
        }
    },
    NONE(EnumSet.noneOf(AppFormTypeEnum.class)) {
        @Override
        public String toString() {
            return "000";
        }
    };

    private EnumSet<AppFormTypeEnum> docs;

    AppFormProductType(EnumSet<AppFormTypeEnum> docs) {
        this.docs = docs;
    }

    public EnumSet<AppFormTypeEnum> getDocs() {
        return docs;
    }
}
