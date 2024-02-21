package th.co.scb.onboardingapp.model.common;

public enum AppFormDocumentType {
    P029_KYC_101 {
        @Override
        public String toString() {
            return "P029";
        }
    },

    NONE {
        @Override
        public String toString() {
            return "0000";
        }
    }
}