package com.example.moja.pfa;

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

    public Integer getNoOfMonthBetween(Date startingDate, Date endDate) {
        return (endDate.year-startingDate.year)*12+(endDate.month-startingDate.month)+1;
    }

    public Integer noOfMonthInRange(Date startingDate) {
        return (this.year-startingDate.year)*12+(this.month-startingDate.month);
    }

    public String addMonth(int iterator) {

        int month = (this.month + iterator-1)%12 +1;
        int year = this.year + (this.month + iterator-1)/12;
        Date temp = new Date(day, month, year);

        return temp.toString();
    }

    public String toString(){
        String dateToReturn="";
        if(day>9)
            dateToReturn+=day.toString();
        else
            dateToReturn+= "0"+day.toString();

        dateToReturn+="/";

        if(month>9)
            dateToReturn+=month.toString();
        else
            dateToReturn+= "0"+month.toString();

        dateToReturn+="/"+year.toString();

        return dateToReturn;
    }
}
