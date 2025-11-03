SELECT o.* FROM eventing.outbox o
INNER JOIN eventing.event_log e ON o.event_id = e.id
WHERE o.attempts < :maxAttempts
AND e.aggregate_type = :aggregateTypes
AND (o.next_attempt_at <= CURRENT_TIMESTAMP OR o.next_attempt_at IS NULL)
AND NOT EXISTS (
    SELECT 1 FROM eventing.outbox o2
    JOIN eventing.event_log e2 ON o2.event_id = e2.id
    WHERE e2.aggregate_id = e.aggregate_id
    AND (o2.next_attempt_at > CURRENT_TIMESTAMP)
)
ORDER BY e.aggregate_id, e.aggregate_version
FOR UPDATE SKIP LOCKED
