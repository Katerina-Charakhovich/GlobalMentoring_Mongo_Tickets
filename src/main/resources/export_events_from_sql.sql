COPY (SELECT row_to_json(results)
FROM (
  SELECT events.id as _id,events.title, events.date,events.ticket_price,
	(
      SELECT array_to_json(array_agg(t))
      FROM (
        SELECT id
        FROM tickets
        WHERE tickets.event_id = events.id
      ) t
    ) AS tickets
  FROM events
) results) TO 'c:\copy\1\events.json' WITH (FORMAT text, HEADER FALSE);