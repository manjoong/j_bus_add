package item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 마루소프트 on 2018-05-02.
 */

public class ExpListItem {
    public ExpListItem() {
        results = new ArrayList<>();
    }


    private ArrayList<Data> results;  //여기서 results를 맞춰줘야해

    public ArrayList<Data> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Talk_CallBackItem{" +
                "results=" + results +
                '}';
    }

    public class Data implements Serializable {
        String terminalId;
        String terminalName;


        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
        }

        public Data(String terminalId, String terminalName) {
            this.terminalId = terminalId;
            this.terminalName = terminalName;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "terminalId='" + terminalId + '\'' +
                    ", terminalName='" + terminalName + '\'' +
                    '}';
        }
    }
}