/*
 * Copyright (c) Crio.Do 2019. All rights reserved
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import log.UncaughtExceptionHandler;
import org.apache.logging.log4j.ThreadContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.springframework.format.annotation.DateTimeFormat;


public class WeatherAdvisorApplication {

    // TODO: CRIO_TASK_MODULE_WEATHER_PARSING - Implement Weather Parser.
    // If you run the following from command line, expected output is 'Yes'.
    // ./gradlew bootrun -q -Pargs=weather_bangalore.json,"2019-05-23 08:15","2019-05-24 09:15"
    // If you run the following from command line, expected outputs is 'No'.
    // ./gradlew -q bootrun -Pargs=weather_bangalore.json,"2019-05-22 08:15","2019-05-22 09:15"
    // For a given period, answer whether rain is expected or not.
    // - If rain is expected, print 'Yes' on the console without quotes.
    // - Else print 'No' without quotes.
    // Steps:
    // 1. Parse the given weather report json file.
    // 2. Answer if rain is expected during the given trip time.
    // @param args command line arguments
    //     args[0] - Filename containing the weather report in json format.
    //     args[1] - Trip start time
    //     args[2] - Trip end time


    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
        ThreadContext.put("runId", UUID.randomUUID().toString());

        // note: use LocalDateTime for parsing datetime
        // Please refer to the Module reading links for more information

        Map<Integer, String> LookUpTable =  new HashMap<Integer, String>();
        LookUpTable.put(230, "02:30");
        LookUpTable.put(530, "05:30");
        LookUpTable.put(830, "08:30");
        LookUpTable.put(1130, "11:30");
        LookUpTable.put(1430, "14:30");
        LookUpTable.put(1730, "17:30");
        LookUpTable.put(2030 ,"20:30");
        LookUpTable.put(2330, "23:30");

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(ClassLoader.getSystemClassLoader().getResource("weather_bangalore.json").getFile()));
            JsonNode Daysnode = root.path("Days");


            // Formatter for the date as well as time.
            DateTimeFormatter formatter1 =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		
	    // Formatter for the date 
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String argv = args[1];
            String argv2 = args[2];

            
	    // Gets the date part ie "2018-10-19 09:54" -> parses ["2018-10-19","09:54"]
	    String[] date_i = argv.split(" ");
            String[] date_f = argv2.split(" ");

            // Switch Variable
            Integer Value = 1;


            // Follows the schema of 2nd Json. Here's the link to it - https://jsonformatter.org/json-viewer/730524
            if(Daysnode.isArray())
            {   
                // Iterate Throughout the JSON
                for(JsonNode dnode: Daysnode)
                {	
		    //Parses Date from JSON
                    String datei1 = dnode.path("date").asText();
                    String date1i = datei1.replaceAll("/","-") ;    // String manipulation to put the string into correct format
                    String[] arfd = date1i.split("-");
                    //System.out.println(date1i);
                    String hsp1 = arfd[2]+"-"+arfd[1]+"-"+arfd[0];  

                    String[] date_i1 = date_i[0].split("-");        // ["2019-09-18"] ->  ["2019","09","18"]
                    String[] date_i2 = date_f[0].split("-");
		    
		    // Converting string object to LocalDate
                    LocalDate date1 = LocalDate.of(Integer.parseInt(date_i1[0]),Integer.parseInt(date_i1[1]),Integer.parseInt(date_i1[2]));
                    LocalDate date2 = LocalDate.of(Integer.parseInt(date_i2[0]),Integer.parseInt(date_i2[1]),Integer.parseInt(date_i2[2]));
                    LocalDate date3 = LocalDate.of(Integer.parseInt(arfd[2]),Integer.parseInt(arfd[1]),Integer.parseInt(arfd[0]));
			
                    //System.out.println("fd1 :"+date1);
                    //System.out.println("fd2 :"+date2);
                    //System.out.println("fd3 :"+date3);
 			
		    // Iterating Throughout the Timeframes
                    JsonNode Timenode = dnode.path("Timeframes");
                    for (JsonNode tnode : Timenode) {
		
                        //     System.out.println("\n ");
                        //    System.out.println("Day :" + date3);
                        //     System.out.println("Day :" + date1);
                        //    System.out.println("Day :" + date2);
                        //    System.out.println("\n ");

                        //    System.out.println((date3.isAfter(date1)));
                        //    System.out.println((date3.isBefore(date2)));
                        
			boolean tem1;
			// if parsed date is after the initial date given through the argv[1]
                        if ((date3.isAfter(date1))) tem1 = true;
                        else tem1 = false;

                        boolean tem2;
			
			// if parsed date is after the initial date given through the argv[1]
                        if ((date3.isBefore(date2))) tem2 = true;
                        else tem2 = false;
                        boolean tem;
                        if (tem1 && tem2) tem = true;
                        else tem = false;
                        //System.out.println(tem+" "+tem1+" "+tem2);

			// if parsed date = 27 and have to be cheked between 26 and 28
                        if (tem||((date3.isEqual(date2)))||((date3.isEqual(date1)))){
                            //System.out.println("working for :" + dt3);
			    
			    //Parsing the exact time
                            String time1 = tnode.path("time").asText();
                            int time12 = Integer.parseInt(time1);
			    
			    //Converts the unformatted time into formatted string ie 520 -> 05:20
                            String time21 = LookUpTable.get(time12);
                            String dateif1 = hsp1 + " " + time21 ;
                            //System.out.println("Date4 :" + date4);

                            LocalDateTime ldt = LocalDateTime.parse(argv, formatter1);
                            LocalDateTime ldt1 = LocalDateTime.parse(argv2, formatter1);
                            LocalDateTime ldt3 = LocalDateTime.parse(dateif1, formatter1);

                            //      System.out.println((ldt3.isAfter(ldt)));
                            //      System.out.println(ldt+" "+ldt1+" "+ldt3);

			    // Now Along with date it also checks time such as "26.
                            if (((ldt3.isAfter(ldt))&&(ldt3.isBefore(ldt1)))||(ldt3.isEqual(ldt1))||(ldt3.isEqual(ldt)))
                            {
                                //System.out.println(date4);
                                String desc1 = tnode.path("wx_desc").asText();
                                int rain = tnode.path("rain_mm").asInt();
                                Double precip = tnode.path("precip_mm").asDouble();
                                //System.out.println("Day :" + date3);
                                //System.out.println("Desc :" + desc1);
                                //System.out.println("Time :" + time1);
                                boolean isFound = desc1.contains("rain");
                                if ((rain > 0 || precip > 0.1) || isFound)
                                {
                                    //System.out.println(ldt3);
                                    Value = 0;
                                    break;
                                }
                            }
                            else
                            {

                                continue;
                            }
                        }

                        else
                        {
                            continue;
                        }
                    }

                }
            }
            if(Value == 1)
                System.out.println("No");
            else if(Value == 0)
                System.out.println("Yes");


        }

        catch(FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) {e.printStackTrace();}
        catch (Exception e) {e.printStackTrace();}



        // note: use LocalDateTime for parsing datetime
        // Please refer to the Module reading links for more information

    }
}
