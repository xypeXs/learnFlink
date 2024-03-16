package ru.rsatu.cursach.utils;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Map;
import java.util.UUID;

public class InvestAggregatedDataDBSinkFunction extends AbstractDBSinkFunction<InvestAggregatedData> {

    private final String SQL = "INSERT INTO invest_sliding_window_info(invest_sliding_window_info_id, asset_code, min_price, max_price, avg_price, assets_count, window_size_millis, min_event_timestamp, max_event_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final Long windowSizeMilliSeconds;

    public InvestAggregatedDataDBSinkFunction(Long windowSizeMilliSeconds) {
        super();
        this.windowSizeMilliSeconds = windowSizeMilliSeconds;
    }

    @Override
    public void processSql(InvestAggregatedData data, Session session) {
        Query<?> insertQuery = session.createNativeQuery(SQL, Void.class);

        extractParameters(data).forEach(insertQuery::setParameter);

        insertQuery.executeUpdate();
    }

    public Map<Integer, Object> extractParameters(InvestAggregatedData data) {
        return Map.of(
                1, UUID.randomUUID(),
                2, data.getAssetCode(),
                3, data.getMinPrice(),
                4, data.getMaxPrice(),
                5, data.getAvgPrice(),
                6, data.getCounter(),
                7, windowSizeMilliSeconds,
                8, data.getMinTimestamp(),
                9, data.getMaxTimestamp()
        );
    }
}
