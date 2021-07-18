// Project 4 EECS3421B
// Written by Judith Dick SID 215045081   2020-12-06
  

import java.util.*;

import javax.sound.midi.SysexMessage;

import java.net.*;
import java.text.*;
import java.lang.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;

import pgpass.*; //home 

public class CreateQuest {
    private Connection conDB;        // Connection to the database system.
    private String url;              // URL: Which database?
    private String user = "jdick";   // Database user account

    private Date day;
    private String realm;
    private String theme; 
    private Integer amount;
    private Float seed;


    // Constructor
    public CreateQuest (String[] args) {
        // Set up the DB connection.
        try {
            // Register the driver with DriverManager.
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // URL: Which database?
        //url = "jdbc:postgresql://db:5432/<dbname>?currentSchema=yrb";
        url = "jdbc:postgresql://db:5432/";

        // Check args
        if (args.length < 4) {
            // Don't know what's wanted.  Bail.
            System.out.println("\nParams: CreateQuest <day> <realm> <theme> <amount> [<user>] [seed]");
            System.exit(0);
        } else {
            try {
                day = Date.valueOf(args[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("\nUsage: java Date day");
                System.out.println("Provide a DATE in yyyy-[m]m-[d]d format.");
                System.exit(0);
            }

            try {
                realm = args[1];
                theme = args[2];
                if(args.length > 4) {
                    user = args[4];
                }
            } catch (Exception e) {
                System.out.println("Provide a Realm and Theme.");
                System.exit(0);
            }

            try {
                amount = Integer.parseInt(args[3]);
            } catch (Exception e) {
                System.out.println("\nUsage: Java Integer amount");
                System.out.println("Provide an amount.");
                System.exit(0);
            }

            if(args.length > 5){
                try {
                    seed = Float.parseFloat(args[5]);
                }catch (Exception e) {
                    System.out.println("Provide a valid (real) seed.");
                    System.exit(0);
                }
            }else {
                //set seed to random number
                Random random = new Random();
                seed = random.nextFloat() * 2 - 1;;
            }



        }
                
    // set up acct info
        // fetch the PASSWD from <.pgpass>
        Properties props = new Properties();
        try {
            String passwd = PgPass.get("db", "*", user, user);
            props.setProperty("user",    user);
            props.setProperty("password", passwd);
            // props.setProperty("ssl","true"); // NOT SUPPORTED on DB
        } catch(PgPassException e) {
            System.out.print("\nCould not obtain PASSWD from <.pgpass>.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Initialize the connection.
        try {
            // Connect with a fall-thru id & password
            //conDB = DriverManager.getConnection(url,"<username>","<password>");
            conDB = DriverManager.getConnection(url, props);
        } catch(SQLException e) {
            System.out.print("\nSQL: database connection error.\n");
            System.out.println(e.toString());
            System.exit(0);
        }    

                // Let's have autocommit turned off.  No particular reason here.
        try {
            conDB.setAutoCommit(false);
        } catch(SQLException e) {
            System.out.print("\nFailed trying to turn autocommit off.\n");
            e.printStackTrace();
            System.exit(0);
        }    

            //check day is not in future:
        if(!dateCheck()){
            System.out.print(day);
            System.out.println(" is not in the future.");
            System.out.println("Bye.");
            System.exit(0);
        }

            // check realm exists
        if(!realmExists()){
            System.out.print("There is no Realm ");
            System.out.print(realm);
            System.out.println(" in the database.");
            System.out.println("Bye.");
            System.exit(0);
        }

            //check amount does not exceed what is possible

            
        if(!validSeed()){
            System.out.print("Seed ");
            System.out.print(seed);
            System.out.println(" must be a real between -1 and 1.");
            System.out.println("Bye.");
            System.exit(0);
        }

        // Do the thing
        addQuest();

        // Commit.  Okay, here nothing to commit really, but why not...
        try {
            conDB.commit();
        } catch(SQLException e) {
            System.out.print("\nFailed trying to commit.\n");
            e.printStackTrace();
            System.exit(0);
        }    
        // Close the connection.
        try {
            conDB.close();
        } catch(SQLException e) {
            System.out.print("\nFailed trying to close the connection.\n");
            e.printStackTrace();
            System.exit(0);
        }   


    }

    public boolean dateCheck(){
        return day.after(new java.util.Date());
    }

    public boolean realmExists(){
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        ResultSet         answers   = null;   // A cursor.

        boolean           inDB      = false;  // Return.

        queryText =
        "SELECT realm       "
      + "FROM realm "
      + "WHERE realm = ?";
    

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, realm);
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?
        try {
            if (answers.next()) {
                inDB = true;
            } else {
                inDB = false;
            }
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch(SQLException e) {
            System.out.print("SQL#1 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch(SQLException e) {
            System.out.print("SQL#1 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        return inDB;
    }


    public boolean validSeed(){
        return (-1 <= seed && seed <= 1);
    }


    public void addQuest() {
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.

        queryText =
            "INSERT INTO  Quest(theme, realm, day)  "
            + "VALUES (?, ?, ?)";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#2 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, theme);
            querySt.setString(2, realm);
            querySt.setDate(3, day);
            querySt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("SQL#2 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        assignLoot();

        // We're done with the handle.
        try {
            querySt.close();
        } catch(SQLException e) {
            System.out.print("SQL#2 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
    }

    public void assignLoot(){
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        ResultSet         answers   = null;   // A cursor.

        String            queryText2 = "";     // The SQL text.
        PreparedStatement querySt2   = null;   // The query handle.


            //~Set Seed 
        queryText =
            "SELECT setseed(?)";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#3 (seed) failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setFloat(1, seed);
            querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#3 (seed) failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

            //~Get list of Treasures
        queryText =
            "SELECT * FROM Treasure  "
            + "ORDER BY random()";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#4 (treasures) failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            // System.out.println(querySt);
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#4 (treasures) failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

            //~Assign the Loot. Fail and rollback if not possible
        Integer loot_id = 1;
        Integer sql_sum = 0;   
        String treasure;

        queryText2 =
        "INSERT INTO Loot(loot_id, treasure, theme, realm, day)"
        + "VALUES (?, ?, ?, ?, ?)";

        // Prepare the query.
        try {
            querySt2 = conDB.prepareStatement(queryText2);
        } catch(SQLException e) {
            System.out.println("SQL#5 (insert loot) failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        //get treasure and sql_amount from cusor and insert loot for quest
        try {
            System.out.println("loot_id, treasure, theme, realm, day, sql, sql_sum");
            while(sql_sum < amount) {
                if (!answers.next()) {
                    // out of answers before sql_sum reaches amount... fail and rollback
                    rollbackQuestLoot();
                    System.out.println("Amount exceeds what is possible.");
                    System.exit(0);
                } else {
                    treasure = answers.getString("treasure");
                    sql_sum += answers.getInt("sql");
                    querySt2.setInt(1, loot_id++);
                    querySt2.setString(2, treasure);
                    querySt2.setString(3, theme);
                    querySt2.setString(4, realm);
                    querySt2.setDate(5, day);
                    querySt2.execute();;
                    System.out.printf("%-3d %-30s %-25s %-15s %-15s %5d %10d    \n", loot_id -1, treasure, theme, realm, day, answers.getInt("sql"), sql_sum);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL#4 failed in cursor or SQL#5 failed to execute.");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch(SQLException e) {
            System.out.print("SQL#2 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handlez.
        try {
            querySt.close();
            querySt2.close();
        } catch(SQLException e) {
            System.out.print("SQL#2 or #5 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
    }


    public void rollbackQuestLoot() {
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.

        queryText =
            "DELETE FROM loot  "
        +   "WHERE theme = ? "
        +   "AND realm = ? "
        +   "AND day = ? ";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#6 (rollback loot) failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, theme);
            querySt.setString(2, realm);
            querySt.setDate(3, day);
            System.out.print("rollback loot ");
            System.out.println(querySt.executeUpdate());
        } catch(SQLException e) {
            System.out.println("SQL#6 (rollback loot) failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

            queryText =
            "DELETE FROM quest  "
        +   "WHERE theme = ? "
        +   "AND realm = ? "
        +   "AND day = ? ";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#7 (rollback quest) failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, theme);
            querySt.setString(2, realm);
            querySt.setDate(3, day);
            System.out.print("rollback quest ");
            System.out.println(querySt.executeUpdate());
        } catch(SQLException e) {
            System.out.println("SQL#7 (rollback quest) failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch(SQLException e) {
            System.out.print("SQL#6+7 (rollback) failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        CreateQuest cq = new CreateQuest(args);
    }

}
