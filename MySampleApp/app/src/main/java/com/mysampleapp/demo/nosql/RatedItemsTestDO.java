package com.mysampleapp.demo.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "shoppingcart-mobilehub-1030480558-RatedItemsTest")

public class RatedItemsTestDO {
    private String _userId;
    private String _itemId;
    private String _barcode;
    private byte[] _picture;
    private Map<String, String> _price;
    private double _quantity;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "itemId")
    @DynamoDBIndexRangeKey(attributeName = "itemId", globalSecondaryIndexName = "Barcode")
    public String getItemId() {
        return _itemId;
    }

    public void setItemId(final String _itemId) {
        this._itemId = _itemId;
    }
    @DynamoDBIndexHashKey(attributeName = "barcode", globalSecondaryIndexName = "Barcode")
    public String getBarcode() {
        return _barcode;
    }

    public void setBarcode(final String _barcode) {
        this._barcode = _barcode;
    }
    @DynamoDBAttribute(attributeName = "picture")
    public byte[] getPicture() {
        return _picture;
    }

    public void setPicture(final byte[] _picture) {
        this._picture = _picture;
    }
    @DynamoDBAttribute(attributeName = "price")
    public Map<String, String> getPrice() {
        return _price;
    }

    public void setPrice(final Map<String, String> _price) {
        this._price = _price;
    }
    @DynamoDBAttribute(attributeName = "quantity")
    public double getQuantity() {
        return _quantity;
    }

    public void setQuantity(final double _quantity) {
        this._quantity = _quantity;
    }

}
