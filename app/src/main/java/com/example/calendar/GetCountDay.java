package com.example.calendar;

public class GetCountDay {


    public int year;
    public int mount;
    boolean vusokosn = false;

    public GetCountDay() {

    }


    public int getCountDay(int year, int mount) {
        int res = 0;
        if (mount >= 1 && mount <= 12) {
            if (year % 4 == 0)
                vusokosn = true;
            else
                vusokosn = false;

            switch (mount) {
                case 1:
                    res = 31;
                    break;

                case 2:


                    if (vusokosn)
                        res = 29;
                    else
                        res = 28;
                    break;

                case 3:
                    res = 31;
                    break;

                case 4:
                    res = 30;
                    break;

                case 5:
                    res = 31;
                    break;

                case 6:
                    res = 30;
                    break;

                case 7:
                    res = 31;
                    break;

                case 8:
                    res = 31;
                    break;


                case 9:
                    res = 30;
                    break;

                case 10:
                    res = 31;
                    break;

                case 11:
                    res = 30;
                    break;

                case 12:
                    res = 31;
                    break;

            }
        }


        return res;
    }

}
