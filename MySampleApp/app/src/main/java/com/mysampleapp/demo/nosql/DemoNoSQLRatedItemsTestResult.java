package com.mysampleapp.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.Set;

public class DemoNoSQLRatedItemsTestResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final RatedItemsTestDO result;

    DemoNoSQLRatedItemsTestResult(final RatedItemsTestDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final String originalValue = result.getBarcode();
        result.setBarcode(DemoSampleDataGenerator.getRandomSampleString("barcode"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setBarcode(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView itemIdKeyTextView;
        final TextView itemIdValueTextView;
        final TextView barcodeKeyTextView;
        final TextView barcodeValueTextView;
        final TextView pictureKeyTextView;
        final TextView pictureValueTextView;
        final TextView priceKeyTextView;
        final TextView priceValueTextView;
        final TextView quantityKeyTextView;
        final TextView quantityValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            itemIdKeyTextView = new TextView(context);
            itemIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(itemIdKeyTextView, itemIdValueTextView);
            layout.addView(itemIdKeyTextView);
            layout.addView(itemIdValueTextView);

            barcodeKeyTextView = new TextView(context);
            barcodeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(barcodeKeyTextView, barcodeValueTextView);
            layout.addView(barcodeKeyTextView);
            layout.addView(barcodeValueTextView);

            pictureKeyTextView = new TextView(context);
            pictureValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(pictureKeyTextView, pictureValueTextView);
            pictureValueTextView.setTypeface(Typeface.MONOSPACE);
            layout.addView(pictureKeyTextView);
            layout.addView(pictureValueTextView);

            priceKeyTextView = new TextView(context);
            priceValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(priceKeyTextView, priceValueTextView);
            layout.addView(priceKeyTextView);
            layout.addView(priceValueTextView);

            quantityKeyTextView = new TextView(context);
            quantityValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(quantityKeyTextView, quantityValueTextView);
            layout.addView(quantityKeyTextView);
            layout.addView(quantityValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            itemIdKeyTextView = (TextView) layout.getChildAt(3);
            itemIdValueTextView = (TextView) layout.getChildAt(4);

            barcodeKeyTextView = (TextView) layout.getChildAt(5);
            barcodeValueTextView = (TextView) layout.getChildAt(6);

            pictureKeyTextView = (TextView) layout.getChildAt(7);
            pictureValueTextView = (TextView) layout.getChildAt(8);

            priceKeyTextView = (TextView) layout.getChildAt(9);
            priceValueTextView = (TextView) layout.getChildAt(10);

            quantityKeyTextView = (TextView) layout.getChildAt(11);
            quantityValueTextView = (TextView) layout.getChildAt(12);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        itemIdKeyTextView.setText("itemId");
        itemIdValueTextView.setText(result.getItemId());
        barcodeKeyTextView.setText("barcode");
        barcodeValueTextView.setText(result.getBarcode());
        pictureKeyTextView.setText("picture");
        pictureValueTextView.setText(bytesToHexString(result.getPicture()));
        priceKeyTextView.setText("price");
        priceValueTextView.setText(result.getPrice().toString());
        quantityKeyTextView.setText("quantity");
        quantityValueTextView.setText("" + (long) result.getQuantity());
        return layout;
    }
}
