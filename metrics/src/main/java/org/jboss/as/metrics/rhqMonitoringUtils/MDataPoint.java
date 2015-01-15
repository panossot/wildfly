/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.metrics.rhqMonitoringUtils;

import java.util.Map;

/**
 *
 * @author panos
 */
public class MDataPoint {

    long timeStamp;
    Double value;
    private int scheduleId;

    public MDataPoint() {
    }

    public MDataPoint(Map<String,Object> in) {
        timeStamp = (Long) in.get("timeStamp");
        value = (Double) in.get("value");
        scheduleId = (Integer)in.get("scheduleId");
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MDataPoint that = (MDataPoint) o;

        if (scheduleId != that.scheduleId) return false;
        if (timeStamp != that.timeStamp) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (timeStamp ^ (timeStamp >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + scheduleId;
        return result;
    }
}
