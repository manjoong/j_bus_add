package item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 마루소프트 on 2018-05-02.
 */

public class SubItem {
    public SubItem() {
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
        String starttime;
        String waypoint;
        String charge;
        String alongtime;

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getWaypoint() {
            return waypoint;
        }

        public void setWaypoint(String waypoint) {
            this.waypoint = waypoint;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public String getAlongtime() {
            return alongtime;
        }

        public void setAlongtime(String alongtime) {
            this.alongtime = alongtime;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "starttime='" + starttime + '\'' +
                    ", waypoint='" + waypoint + '\'' +
                    ", charge='" + charge + '\'' +
                    ", alongtime='" + alongtime + '\'' +
                    '}';
        }
    }
}