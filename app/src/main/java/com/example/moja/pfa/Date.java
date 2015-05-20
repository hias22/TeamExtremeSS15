package com.example.moja.pfa;



/**
 * Created by Mathias on 09.05.2015.
 */
public class Date {
    Integer day;
    Integer month;
    Integer year;

    public Date(Integer day, Integer month, Integer year){
        this.day=day;
        this.month=month;
        this.year=year;
    }

    public Date(String date){
        int dateSize = date.length();
        char[] dateArray =  date.toCharArray();
        char[] year = new char[4];
        year[3]=dateArray[dateSize-1];
        year[2]=dateArray[dateSize-2];
        year[1]=dateArray[dateSize-3];
        year[0]=dateArray[dateSize-4];

        char[] month = new char[2];
        if(dateArray[dateSize-7] == '/'){
            month[0]='0';
            month[1]=dateArray[dateSize-6];
        }else{
            month[0]=dateArray[dateSize-7];
            month[1]=dateArray[dateSize-6];
        }

        char[] day = new char[2];
        if(dateArray[1] == '/'){
            day[0]='0';
            day[1]=dateArray[0];
        }else{
            day[0]=dateArray[0];
            day[1]=dateArray[1];
        }

        this.day=Integer.parseInt(new String(day));
        this.month=Integer.parseInt(new String(month));
        this.year=Integer.parseInt(new String(year));
    }

    public boolean isDateInRange(Date from, Date to){

        boolean booleanValue = false;

        if((!isDateBefore(from)) && (isDateBefore(to) || isDateTheSame(to) ))
            booleanValue = true;

        return booleanValue;
    }

    public boolean isDateBefore(Date date){
        boolean booleanValue = false;

        if(this.year <= date.year){
            if(this.year < date.year){
                booleanValue = true;
            }else{
                if(this.month <= date.month){
                    if(this.month < date.month){
                        booleanValue = true;
                    }else{
                        if(this.day < date.day){
                            booleanValue = true;
                        }
                    }
                }
            }
        }
        return booleanValue;
    }

    public boolean isDateTheSame(Date date){
        boolean booleanValue = false;

        if((this.year.compareTo(date.year) == 0) && (this.month.compareTo(date.month) == 0) && (this.day.compareTo(date.day) == 0)){
            booleanValue = true;
        }


        return booleanValue;
    }

}
