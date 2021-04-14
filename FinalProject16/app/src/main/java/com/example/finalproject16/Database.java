package com.example.finalproject16;

import android.content.Context;
import android.util.Log;

/*import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.material.tabs.TabLayout;*/

public class Database {
    /*private final String COGNITO_POOL_ID = "us-east-1:7c85fed6-f01d-455b-a972-5d8623351e94";
    private final Regions COGNITO_REGION = Regions.US_EAST_1;
    private final String TABLE = "scavenge_users";
    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;

    private static volatile Database instance;

    private Database(Context context){
        this.context = context;

        credentialsProvider = new CognitoCachingCredentialsProvider(context, COGNITO_POOL_ID, COGNITO_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(COGNITO_REGION));
        dbTable = Table.loadTable(dbClient, TABLE);
    }

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = new Database(context);
        }
        return instance;
    }

    public boolean updateScore (String email, int pointsToAdd) {
        Log.i("DATABASE", "in update");
        Document dataRecieved = dbTable.getItem(new Primitive(email));

        if (dataRecieved != null){
            //int oldScore = dataRecieved.get("score");
            //dataRecieved.put("score", );
        }
        return true;
    }*/
}
