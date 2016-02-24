package com.yilinker.expressinternal.utilities;

import java.text.DecimalFormat;

/**
 * Created by rlcoronado on 24/02/2016.
 */
public class PriceFormatHelper {

    public static String formatPrice(double price) {

        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");
        return String.format("₱ %s", formatter.format(price));

    }
}
