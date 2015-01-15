/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.metrics.metricsMonitoringRhq;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.jboss.as.metrics.rhqMonitoringUtils.DoubleValue;
import org.jboss.as.metrics.rhqMonitoringUtils.Resource;
import org.jboss.as.metrics.rhqMonitoringUtils.Schedule;

/**
 *
 * @author panos
 */
public class MonitoringRhq {

    private static MonitoringRhq mrhq = new MonitoringRhq();

    final String APPLICATION_JSON = "application/json";
    final String WRAPPED_JSON = "application/vnd.rhq.wrapped+json";
    final String REST_METRICS = "-rest-metrics-";
    private Header acceptJson;
    private int _platformId;
    private int _platformTypeId;
    private int numericScheduleId;
    private int numericScheduleDefinitionId;
    private long defaultInterval;
    private String scheduleName;
    private long previousTime;

    private MonitoringRhq() {
        RestAssured.baseURI = "http://" + System.getProperty("rest.server", "localhost");
        RestAssured.port = 7080;
        RestAssured.basePath = "/rest/";
        RestAssured.authentication = basic("rhqadmin", "rhqadmin");

        acceptJson = new Header("Accept", APPLICATION_JSON);

        Resource resource = new Resource();
        resource.setResourceName(REST_METRICS);
        resource.setTypeName("Linux");
        Resource platform
                = given()
                .header(acceptJson)
                .contentType(ContentType.JSON)
                .body(resource)
                .expect()
                .statusCode(201)
                .log().ifError()
                .when()
                .post("/resource/platforms")
                .as(Resource.class);
        _platformId = platform.getResourceId();
        _platformTypeId = platform.getTypeId();

// Determine a schedule id for the common cases
        Response r = given().header(acceptJson)
                .queryParam("type", "metric")
                .pathParam("rid", _platformId)
                .expect()
                .statusCode(200)
                .log().ifError()
                .when()
                .get("/resource/{rid}/schedules");

        JsonPath jp = r.jsonPath();
        System.out.println("rid : " + _platformId);
        numericScheduleId = jp.getInt("[2].scheduleId");
        System.out.println("numericScheduleId : " + numericScheduleId);
        numericScheduleDefinitionId = jp.getInt("[2].definitionId");
        System.out.println("numericScheduleDefinitionId : " + numericScheduleDefinitionId);
        defaultInterval = jp.getLong("[2].collectionInterval");
        System.out.println("defaultInterval : " + defaultInterval);
        scheduleName = jp.getString("[2].scheduleName");
        System.out.println("scheduleName : " + scheduleName);

        Schedule schedule = new Schedule();
        schedule.setCollectionInterval(1);
        schedule.setDisplayName("Metrics");
   //     schedule.setScheduleName("Metrics Test");
        schedule.setEnabled(true);
        given()
                .header(acceptJson)
                .contentType(ContentType.JSON)
                .pathParam("id", numericScheduleId)
                .body(schedule)
                .expect()
                .statusCode(200)
                .log().ifError()
                .when()
                .put("/metric/schedule/{id}");
    }

    public static MonitoringRhq getRhq() {
        return mrhq;
    }

    private static void setupRestAssured() {
        RestAssured.baseURI = "http://" + System.getProperty("rhq.server", "localhost");
        RestAssured.port = Integer.parseInt(System.getProperty("rhq.port", "7080"));
        RestAssured.basePath = "/rest/";
        RestAssured.authentication = basic(System.getProperty("rhq.username", "rhqadmin"), System.getProperty("rhq.password", "rhqadmin"));
    }

    public void setup() {
        setupRestAssured();
        Resource resource = new Resource();
        resource.setResourceName(REST_METRICS);
        resource.setTypeName("Linux");
        Resource platform
                = given()
                .header(acceptJson)
                .contentType(ContentType.JSON)
                .body(resource)
                .expect()
                .statusCode(201)
                .log().ifError()
                .when()
                .post("/resource/platforms")
                .as(Resource.class);
        _platformId = platform.getResourceId();
        _platformTypeId = platform.getTypeId();

// Determine a schedule id for the common cases
        Response r = given().header(acceptJson)
                .queryParam("type", "metric")
                .pathParam("rid", _platformId)
                .expect()
                .statusCode(200)
                .log().ifError()
                .when()
                .get("/resource/{rid}/schedules");

        JsonPath jp = r.jsonPath();
        System.out.println("rid : " + _platformId);
        numericScheduleId = jp.getInt("[2].scheduleId");
        System.out.println("numericScheduleId : " + numericScheduleId);
        numericScheduleDefinitionId = jp.getInt("[2].definitionId");
        System.out.println("numericScheduleDefinitionId : " + numericScheduleDefinitionId);
        defaultInterval = jp.getLong("[2].collectionInterval");
        System.out.println("defaultInterval : " + defaultInterval);
        scheduleName = jp.getString("[2].scheduleName");
        System.out.println("scheduleName : " + scheduleName);
    }

    public boolean rhqMonitoring(String data) {
        boolean dataSent = false;

        long now = System.currentTimeMillis();
        DoubleValue dataPoint = new DoubleValue(Double.parseDouble(data));
        given()
                .header(acceptJson)
                .contentType(ContentType.JSON)
                .pathParam("id", numericScheduleId)
                .pathParam("timestamp", now)
                .body(dataPoint)
                .expect()
                .statusCode(201)
                .log().ifError()
                .when()
                .put("/metric/data/{id}/raw/{timestamp}");
        /*     Response response = given()
         .header(acceptJson)
         .pathParam("id", numericScheduleId)
         .queryParam("startTime", now - 10)
         .queryParam("endTime", now + 10)
         .expect()
         .statusCode(200)
         .log().ifError()
         .when()
         .get("/metric/data/{id}/raw");
         List<Map<String, Object>> list = response.as(List.class);
         boolean found = false;
         for (Map<String, Object> map : list) {
         MDataPoint mp = new MDataPoint(map);
         if (mp.getTimeStamp() == now && mp.getScheduleId() == numericScheduleId) {
         found = true;
         System.out.println("DataPoint Found : " + mp.getScheduleId() + " Value : " + mp.getValue());
         }
         }*/

        return dataSent;
    }

    public void disconnect() {
        List res
                = given()
                .header(acceptJson)
                .queryParam("q", REST_METRICS)
                .expect()
                .statusCode(200)
                .log().ifError()
                .when()
                .get("/resource")
                .as(List.class);
        if (res != null && res.get(0) != null) {
            Integer pid = ((Map<String, Integer>) res.get(0)).get("resourceId");
            given()
                    .pathParam("id", pid)
                    .expect()
                    .statusCode(204)
                    .log().ifError()
                    .when()
                    .delete("/resource/{id}");
        }
    }
}
