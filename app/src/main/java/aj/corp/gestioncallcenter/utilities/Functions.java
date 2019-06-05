package aj.corp.gestioncallcenter.utilities;

public class Functions {

    public static String convertToMonth(int month){
        switch (month){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";

            default:
                return "Enero";
        }
    }

    public static String DateSimpleConversion(String date){
        String[] datos = date.split("-");
        String year = datos[0];
        String month = datos[1];
        String day = datos[2];

        return day+"/"+month+"/"+year;
    }

    public static String toMonthConversion(int month, int year){
        String mes = ""+month;
        if(month < 10){
            mes = "0"+month;
        }
        return ""+year+"-"+mes;
    }

}
