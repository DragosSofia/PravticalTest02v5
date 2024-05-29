package ro.pub.cs.systems.eim.practicaltest02v5;

public class KeyInfo {

        private String value;
        private String timestamp;

        public KeyInfo() {
            this.value = null;
            this.timestamp = null;
        }

        public KeyInfo(String value, String timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "KeyInfo{" +
                    "value='" + value + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
}
