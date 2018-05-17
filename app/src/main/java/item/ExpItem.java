package item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 마루소프트 on 2018-05-02.
 */

public class ExpItem {
    public ExpItem() {
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
        String arrName;
        String arrTime;
        String charge;
        String depName;
        String depTime;
        String grade;
        String route;

        public String getArrName() {
            return arrName;
        }

        public void setArrName(String arrName) {
            this.arrName = arrName;
        }

        public String getArrTime() {
            return arrTime;
        }

        public void setArrTime(String arrTime) {
            this.arrTime = arrTime;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getDepTime() {
            return depTime;
        }

        public void setDepTime(String depTime) {
            this.depTime = depTime;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public Data(String arrName, String arrTime, String charge, String depName, String depTime, String grade, String route) {
            this.arrName = arrName;
            this.arrTime = arrTime;
            this.charge = charge;
            this.depName = depName;
            this.depTime = depTime;
            this.grade = grade;
            this.route = route;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "arrName='" + arrName + '\'' +
                    ", arrTime='" + arrTime + '\'' +
                    ", charge='" + charge + '\'' +
                    ", depName='" + depName + '\'' +
                    ", depTime='" + depTime + '\'' +
                    ", grade='" + grade + '\'' +
                    ", route='" + route + '\'' +
                    '}';
        }
    }
}