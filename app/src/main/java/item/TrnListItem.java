package item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 마루소프트 on 2018-05-02.
 */

public class TrnListItem {
    public TrnListItem() {
        body = new ArrayList<>();
    }


    private ArrayList<Data> body;  //여기서 results를 맞춰줘야해

    public ArrayList<Data> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Talk_CallBackItem{" +
                "body=" + body +
                '}';
    }

    public class Data implements Serializable {
        String traincode;
        String trainname;

        public String getTraincode() {
            return traincode;
        }

        public void setTraincode(String traincode) {
            this.traincode = traincode;
        }

        public String getTrainname() {
            return trainname;
        }

        public void setTrainname(String trainname) {
            this.trainname = trainname;
        }

        public Data(String traincode, String trainname) {
            this.traincode = traincode;
            this.trainname = trainname;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "traincode='" + traincode + '\'' +
                    ", trainname='" + trainname + '\'' +
                    '}';
        }
    }
}