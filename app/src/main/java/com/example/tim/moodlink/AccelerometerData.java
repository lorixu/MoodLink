package com.example.tim.moodlink;


public class AccelerometerData extends SensorData {

    private int id;
    private double xAxisValue;
    private double yAxisValue;
    private double zAxisValue;


    public AccelerometerData(double xAxisValue, double yAxisValue, double zAxisValue,TimeAndDay time){
        super(time);
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
        this.zAxisValue = zAxisValue;
    }

    public AccelerometerData(double xAxisValue, double yAxisValue, double zAxisValue,int day, int month, int year, int hour, int minute, int second){
        super(day,month,year,hour,minute,second);
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
        this.zAxisValue = zAxisValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getxAxisValue() {
        return xAxisValue;
    }

    public double getyAxisValue() {
        return yAxisValue;
    }

    public double getzAxisValue() {
        return zAxisValue;
    }

    @Override
    public String toString() {
        return "\nAccelerometerData{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", xAxisValue=" + xAxisValue +
                ", yAxisValue=" + yAxisValue +
                ", zAxisValue=" + zAxisValue +
                '}';
    }
}
