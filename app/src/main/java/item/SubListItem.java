package item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 마루소프트 on 2018-05-02.
 */

public class SubListItem {
    public SubListItem() {
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
        String tername;

        public String getTername() {
            return tername;
        }

        public void setTername(String tername) {
            this.tername = tername;
        }

        public Data(String tername) {
            this.tername = tername;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tername='" + tername + '\'' +
                    '}';
        }
    }
}