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
    private String _category;
    private String _details;
    private String _name;
    private double _price;
    private double _ratingCount;
    private double _ratingValue;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "itemId")
    @DynamoDBAttribute(attributeName = "itemId")
    public String getItemId() {
        return _itemId;
    }

    public void setItemId(final String _itemId) {
        this._itemId = _itemId;
    }
    @DynamoDBIndexHashKey(attributeName = "category", globalSecondaryIndexName = "Ratings")
    public String getCategory() {
        return _category;
    }

    public void setCategory(final String _category) {
        this._category = _category;
    }
    @DynamoDBAttribute(attributeName = "details")
    public String getDetails() {
        return _details;
    }

    public void setDetails(final String _details) {
        this._details = _details;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "price")
    public double getPrice() {
        return _price;
    }

    public void setPrice(final double _price) {
        this._price = _price;
    }
    @DynamoDBAttribute(attributeName = "ratingCount")
    public double getRatingCount() {
        return _ratingCount;
    }

    public void setRatingCount(final double _ratingCount) {
        this._ratingCount = _ratingCount;
    }
    @DynamoDBIndexRangeKey(attributeName = "ratingValue", globalSecondaryIndexName = "Ratings")
    public double getRatingValue() {
        return _ratingValue;
    }

    public void setRatingValue(final double _ratingValue) {
        this._ratingValue = _ratingValue;
    }

}
