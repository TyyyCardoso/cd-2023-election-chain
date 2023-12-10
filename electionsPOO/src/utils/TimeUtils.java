//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2015   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author António Manso
 */
public class TimeUtils {

    public static DateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static DateFormat time = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat precisionTime = new SimpleDateFormat("mm:ss.SS");

    public static String toTime(long time) {
        return precisionTime.format(new Date(time));
    }

    public static String toTime(Date d) {
        return precisionTime.format(d);
    }
   

}
