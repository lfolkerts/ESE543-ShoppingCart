package com.mysampleapp.demo.nosql;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.mysampleapp.R;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DemoNoSQLTableRatedItemsTest extends DemoNoSQLTableBase {
    private static final String LOG_TAG = DemoNoSQLTableRatedItemsTest.class.getSimpleName();

    /** Condition demo key for Primary Queries with filter conditions. */
    private static final String DEMO_PRIMARY_CONDITION_KEY = "details";
    /** Condition demo value for Queries with filter conditions. */
    private static final String DEMO_PRIMARY_CONDITION_VALUE = "demo-" + DEMO_PRIMARY_CONDITION_KEY + "-500000";

    /** Condition demo key for Secondary Queries with filter conditions. */
    private static final String DEMO_SECONDARY_CONDITION_KEY = "itemId";
    /** Condition demo value for Queries with filter conditions. */
    private static final String DEMO_SECONDARY_CONDITION_VALUE = "demo-" + DEMO_SECONDARY_CONDITION_KEY + "-500000";

    private static final String DEMO_PARTITION_KEY = "userId";
    /** Partition/Hash demo value for Get and Queries. */
    private String getDemoPartitionValue() {
        return cognitoIdentityId;
    }
    private String getDemoPartitionValueText() {
        return cognitoIdentityId;
    }
    /** The Primary Partition Key Name.  All queries must use an equality condition on this key. */
    /** The Primary Sort Key Name. */
    private static final String DEMO_SORT_KEY = "itemId";
    /** Sort/Range demo value for Get and Queries. */
    private static final String DEMO_SORT_VALUE = "demo-" + DEMO_SORT_KEY + "-500000";
    private static final String DEMO_SORT_VALUE_TEXT = DEMO_SORT_VALUE;

    /********* Primary Get Query Inner Classes *********/

    public class DemoGetWithPartitionKeyAndSortKey extends DemoNoSQLOperationBase {
        private RatedItemsTestDO result;
        private boolean resultRetrieved = true;

        DemoGetWithPartitionKeyAndSortKey(final Context context) {
            super(context.getString(R.string.nosql_operation_get_by_partition_and_sort_text),
                String.format(context.getString(R.string.nosql_operation_example_get_by_partition_and_sort_text),
                    DEMO_PARTITION_KEY, getDemoPartitionValueText(),
                    DEMO_SORT_KEY, DEMO_SORT_VALUE_TEXT));
        }

        @Override
        public boolean executeOperation() {
            // Retrieve an item by passing the partition key using the object mapper.
            result = mapper.load(RatedItemsTestDO.class, getDemoPartitionValue(), DEMO_SORT_VALUE);

            if (result != null) {
                resultRetrieved = false;
                return true;
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            if (resultRetrieved) {
                return null;
            }
            final List<DemoNoSQLResult> results = new ArrayList<>();
            results.add(new DemoNoSQLRatedItemsTestResult(result));
            resultRetrieved = true;
            return results;
        }

        @Override
        public void resetResults() {
            resultRetrieved = false;
        }
    }

    /* ******** Primary Index Query Inner Classes ******** */

    public class DemoQueryWithPartitionKeyAndSortKeyCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoQueryWithPartitionKeyAndSortKeyCondition(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_and_sort_condition_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_and_sort_condition_text),
                      DEMO_PARTITION_KEY, getDemoPartitionValueText(),
                      DEMO_SORT_KEY, DEMO_SORT_VALUE_TEXT));
        }

        @Override
        public boolean executeOperation() {
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setUserId(getDemoPartitionValue());

            final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withS(DEMO_SORT_VALUE));
            final DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition(DEMO_SORT_KEY, rangeKeyCondition)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets the next page of results from the query.
         * @return list of results, or null if there are no more results.
         */
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoQueryWithPartitionKeyOnly extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoQueryWithPartitionKeyOnly(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_text),
                String.format(context.getString(R.string.nosql_operation_example_query_by_partition_text),
                    DEMO_PARTITION_KEY, getDemoPartitionValueText()));
        }

        @Override
        public boolean executeOperation() {
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setUserId(getDemoPartitionValue());

            final DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoQueryWithPartitionKeyAndFilter extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoQueryWithPartitionKeyAndFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_and_filter_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_and_filter_text),
                      DEMO_PARTITION_KEY, getDemoPartitionValueText(),
                      DEMO_PRIMARY_CONDITION_KEY, DEMO_PRIMARY_CONDITION_VALUE));
        }

        @Override
        public boolean executeOperation() {
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setUserId(getDemoPartitionValue());

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#details", DEMO_PRIMARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":Mindetails",
                new AttributeValue().withS(DEMO_PRIMARY_CONDITION_VALUE));

            final DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withFilterExpression("#details > :Mindetails")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
             resultsIterator = results.iterator();
         }
    }

    public class DemoQueryWithPartitionKeySortKeyConditionAndFilter extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoQueryWithPartitionKeySortKeyConditionAndFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_sort_condition_and_filter_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_sort_condition_and_filter_text),
                      DEMO_PARTITION_KEY, getDemoPartitionValueText(),
                      DEMO_SORT_KEY, DEMO_SORT_VALUE_TEXT,
                      DEMO_PRIMARY_CONDITION_KEY, DEMO_PRIMARY_CONDITION_VALUE));
        }

        public boolean executeOperation() {
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setUserId(getDemoPartitionValue());

            final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withS(DEMO_SORT_VALUE));

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#details", DEMO_PRIMARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":Mindetails",
                new AttributeValue().withS(DEMO_PRIMARY_CONDITION_VALUE));

            final DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition(DEMO_SORT_KEY, rangeKeyCondition)
                .withFilterExpression("#details > :Mindetails")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /* ******** Secondary Named Index Query Inner Classes ******** */

    private static final String DEMO_RATINGS_PARTITION_KEY = "category";
    private static final String DEMO_RATINGS_PARTITION_VALUE = "demo-category-3";
    private static final String DEMO_RATINGS_PARTITION_VALUE_TEXT = DEMO_RATINGS_PARTITION_VALUE;

    private String getDemoRatingsPartitionValue() {
        return DEMO_RATINGS_PARTITION_VALUE;
    }

    private String getDemoRatingsPartitionValueText() {
        return DEMO_RATINGS_PARTITION_VALUE_TEXT;
    }

    private static final String DEMO_RATINGS_SORT_KEY = "ratingValue";
    private static final Double DEMO_RATINGS_SORT_VALUE = 1111500000.0;
    private static final String DEMO_RATINGS_SORT_VALUE_TEXT = "1111500000";

    public class DemoRatingsQueryWithPartitionKeyAndSortKeyCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;
        DemoRatingsQueryWithPartitionKeyAndSortKeyCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_sort_condition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_sort_condition_text,
                    DEMO_RATINGS_PARTITION_KEY, getDemoRatingsPartitionValueText(),
                    DEMO_RATINGS_SORT_KEY, DEMO_RATINGS_SORT_VALUE_TEXT));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and sort key condition.
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setCategory(getDemoRatingsPartitionValue());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())

                .withAttributeValueList(new AttributeValue().withN(DEMO_RATINGS_SORT_VALUE.toString()));
            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("ratingValue", sortKeyCondition)
                .withConsistentRead(false);
            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoRatingsQueryWithPartitionKeyOnly extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoRatingsQueryWithPartitionKeyOnly(final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_text,
                    DEMO_RATINGS_PARTITION_KEY, getDemoRatingsPartitionValueText()));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setCategory(getDemoRatingsPartitionValue());

            // Perform get using Partition key
            DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withConsistentRead(false);
            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoRatingsQueryWithPartitionKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoRatingsQueryWithPartitionKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_filter_text,
                    DEMO_RATINGS_PARTITION_KEY, getDemoRatingsPartitionValueText(),
                    DEMO_SECONDARY_CONDITION_KEY, DEMO_SECONDARY_CONDITION_VALUE));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setCategory(getDemoRatingsPartitionValue());

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#itemId", DEMO_SECONDARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinitemId",
                new AttributeValue().withS(DEMO_SECONDARY_CONDITION_VALUE));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withFilterExpression("#itemId > :MinitemId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoRatingsQueryWithPartitionKeySortKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoRatingsQueryWithPartitionKeySortKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_sort_condition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_sort_condition_and_filter_text,

                    DEMO_RATINGS_PARTITION_KEY, getDemoRatingsPartitionValueText(),
                    DEMO_RATINGS_SORT_KEY, DEMO_RATINGS_SORT_VALUE,
                    DEMO_SECONDARY_CONDITION_KEY, DEMO_SECONDARY_CONDITION_VALUE));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key, sort condition, and filter.
            final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
            itemToFind.setCategory(getDemoRatingsPartitionValue());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())

                .withAttributeValueList(new AttributeValue().withN(DEMO_RATINGS_SORT_VALUE.toString()));
            // Use a map of expression names to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map<String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#itemId", DEMO_SECONDARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinitemId",
                new AttributeValue().withS(DEMO_SECONDARY_CONDITION_VALUE));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("ratingValue", sortKeyCondition)
                .withFilterExpression("#itemId > :MinitemId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(RatedItemsTestDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /********* Scan Inner Classes *********/

    public class DemoScanWithFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoScanWithFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_with_filter),
                String.format(context.getString(R.string.nosql_operation_example_scan_with_filter),
                    DEMO_PRIMARY_CONDITION_KEY, DEMO_PRIMARY_CONDITION_VALUE));
        }

        @Override
        public boolean executeOperation() {
            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#details", DEMO_PRIMARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":Mindetails",
                new AttributeValue().withS(DEMO_PRIMARY_CONDITION_VALUE));
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("#details > :Mindetails")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues);

            results = mapper.scan(RatedItemsTestDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoScanWithoutFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<RatedItemsTestDO> results;
        private Iterator<RatedItemsTestDO> resultsIterator;

        DemoScanWithoutFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_without_filter),
                context.getString(R.string.nosql_operation_example_scan_without_filter));
        }

        @Override
        public boolean executeOperation() {
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            results = mapper.scan(RatedItemsTestDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /**
     * Helper Method to handle retrieving the next group of query results.
     * @param resultsIterator the iterator for all the results (makes a new service call for each result group).
     * @return the next list of results.
     */
    private static List<DemoNoSQLResult> getNextResultsGroupFromIterator(final Iterator<RatedItemsTestDO> resultsIterator) {
        if (!resultsIterator.hasNext()) {
            return null;
        }
        List<DemoNoSQLResult> resultGroup = new LinkedList<>();
        int itemsRetrieved = 0;
        do {
            // Retrieve the item from the paginated results.
            final RatedItemsTestDO item = resultsIterator.next();
            // Add the item to a group of results that will be displayed later.
            resultGroup.add(new DemoNoSQLRatedItemsTestResult(item));
            itemsRetrieved++;
        } while ((itemsRetrieved < RESULTS_PER_RESULT_GROUP) && resultsIterator.hasNext());
        return resultGroup;
    }

    /** Inner classes use this value to determine how many results to retrieve per service call. */
    private final static int RESULTS_PER_RESULT_GROUP = 40;
    /** Removing sample data removes the items in batches of the following size. */
    private static final int MAX_BATCH_SIZE_FOR_DELETE = 50;

    /** The table name. */
    private static final String TABLE_NAME = "RatedItemsTest";
    /** The Primary Partition Key Type. */
    private static final String DEMO_PARTITION_KEY_TYPE = "String";
    /** The Sort Key Type. */
    private static final String DEMO_SORT_KEY_TYPE = "String";
    /** The number of secondary table indexes. */
    private static final int NUM_TABLE_INDEXES = 1;

    /** The DynamoDB object mapper for accessing DynamoDB. */
    private final DynamoDBMapper mapper;

    /** Private and Protected tables must use the Cognito Identity as the hash key, so we must store it. */
    private static String cognitoIdentityId = null;

    public DemoNoSQLTableRatedItemsTest() {
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPartitionKeyName() {
        return DEMO_PARTITION_KEY;
    }

    public String getPartitionKeyType() {
        return DEMO_PARTITION_KEY_TYPE;
    }

    @Override
    public String getSortKeyName() {
        return DEMO_SORT_KEY;
    }

    public String getSortKeyType() {
        return DEMO_SORT_KEY_TYPE;
    }

    @Override
    public int getNumIndexes() {
        return NUM_TABLE_INDEXES;
    }


    @Override
    public void insertSampleData() throws AmazonClientException {
        Log.d(LOG_TAG, "Inserting Sample data.");
        final RatedItemsTestDO firstItem = new RatedItemsTestDO();

        firstItem.setUserId(getDemoPartitionValue());
        firstItem.setItemId(DEMO_SORT_VALUE);
        firstItem.setCategory(DemoSampleDataGenerator.getRandomPartitionSampleString("category"));
        firstItem.setDetails(
            DemoSampleDataGenerator.getRandomSampleString("details"));
        firstItem.setName(
            DemoSampleDataGenerator.getRandomSampleString("name"));
        firstItem.setPrice(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setRatingCount(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setRatingValue(DemoSampleDataGenerator.getRandomSampleNumber());
        AmazonClientException lastException = null;

        try {
            mapper.save(firstItem);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }

        final RatedItemsTestDO[] items = new RatedItemsTestDO[SAMPLE_DATA_ENTRIES_PER_INSERT-1];
        for (int count = 0; count < SAMPLE_DATA_ENTRIES_PER_INSERT-1; count++) {
            final RatedItemsTestDO item = new RatedItemsTestDO();
            item.setUserId(cognitoIdentityId);
            item.setItemId(DemoSampleDataGenerator.getRandomSampleString("itemId"));
            item.setCategory(DemoSampleDataGenerator.getRandomPartitionSampleString("category"));
            item.setDetails(DemoSampleDataGenerator.getRandomSampleString("details"));
            item.setName(DemoSampleDataGenerator.getRandomSampleString("name"));
            item.setPrice(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setRatingCount(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setRatingValue(DemoSampleDataGenerator.getRandomSampleNumber());

            items[count] = item;
        }
        try {
            mapper.batchSave(Arrays.asList(items));
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item batch : " + ex.getMessage(), ex);
            lastException = ex;
        }

        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            throw lastException;
        }
    }

    @Override
    public void removeSampleData() throws AmazonClientException {
        final RatedItemsTestDO itemToFind = new RatedItemsTestDO();
        itemToFind.setUserId(getDemoPartitionValue());

        final DynamoDBQueryExpression<RatedItemsTestDO> queryExpression = new DynamoDBQueryExpression<RatedItemsTestDO>()
            .withHashKeyValues(itemToFind)
            .withConsistentRead(false)
            .withLimit(MAX_BATCH_SIZE_FOR_DELETE);

        final PaginatedQueryList<RatedItemsTestDO> results = mapper.query(RatedItemsTestDO.class, queryExpression);

        Iterator<RatedItemsTestDO> resultsIterator = results.iterator();

        AmazonClientException lastException = null;

        if (resultsIterator.hasNext()) {
            final RatedItemsTestDO item = resultsIterator.next();

            // Demonstrate deleting a single item.
            try {
                mapper.delete(item);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item : " + ex.getMessage(), ex);
                lastException = ex;
            }
        }

        final List<RatedItemsTestDO> batchOfItems = new LinkedList<RatedItemsTestDO>();
        while (resultsIterator.hasNext()) {
            // Build a batch of books to delete.
            for (int index = 0; index < MAX_BATCH_SIZE_FOR_DELETE && resultsIterator.hasNext(); index++) {
                batchOfItems.add(resultsIterator.next());
            }
            try {
                // Delete a batch of items.
                mapper.batchDelete(batchOfItems);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item batch : " + ex.getMessage(), ex);
                lastException = ex;
            }

            // clear the list for re-use.
            batchOfItems.clear();
        }


        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            // The logs contain all the exceptions that occurred during attempted delete.
            throw lastException;
        }
    }

    private List<DemoNoSQLOperationListItem> getSupportedDemoOperations(final Context context) {
        List<DemoNoSQLOperationListItem> noSQLOperationsList = new ArrayList<DemoNoSQLOperationListItem>();
        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_get)));
        noSQLOperationsList.add(new DemoGetWithPartitionKeyAndSortKey(context));

        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_primary_queries)));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyOnly(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyAndFilter(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyAndSortKeyCondition(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeySortKeyConditionAndFilter(context));

        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_secondary_queries, "Ratings")));

        noSQLOperationsList.add(new DemoRatingsQueryWithPartitionKeyOnly(context));
        noSQLOperationsList.add(new DemoRatingsQueryWithPartitionKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoRatingsQueryWithPartitionKeyAndSortKeyCondition(context));
        noSQLOperationsList.add(new DemoRatingsQueryWithPartitionKeySortKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_scan)));
        noSQLOperationsList.add(new DemoScanWithoutFilter(context));
        noSQLOperationsList.add(new DemoScanWithFilter(context));
        return noSQLOperationsList;
    }

    @Override
    public void getSupportedDemoOperations(final Context context,
                                           final SupportedDemoOperationsHandler opsHandler) {
        AWSMobileClient
            .defaultMobileClient()
            .getIdentityManager()
            .getUserID(new IdentityManager.IdentityHandler() {
                @Override
                public void handleIdentityID(final String identityId) {
                    cognitoIdentityId = identityId;
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            opsHandler.onSupportedOperationsReceived(getSupportedDemoOperations(context));
                        }
                    });
                }

                @Override
                public void handleError(final Exception exception) {
                    // This should never happen since the Identity ID is retrieved
                    // when the Application starts.
                    cognitoIdentityId = null;
                    opsHandler.onSupportedOperationsReceived(new ArrayList<DemoNoSQLOperationListItem>());
                }
            });
    }
}
