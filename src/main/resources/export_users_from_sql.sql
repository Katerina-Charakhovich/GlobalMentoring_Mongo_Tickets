COPY (SELECT row_to_json(results)
FROM (
  SELECT  CAST(events.id AS VARCHAR(255)) as _id,
	events.title,
	events.date,
	events.ticket_price,(
		SELECT array_to_json(array_agg(t))
		FROM (
			SELECT CAST(id AS VARCHAR(255))
			FROM tickets
			WHERE tickets.event_id = events.id
      ) t
    ) AS tickets
  FROM events
) results) TO 'c:\copy\1\events.json' WITH (FORMAT text, HEADER FALSE);


COPY (SELECT row_to_json(results)
FROM (
  SELECT CAST(users.id AS VARCHAR(255)) as _id,
	users.name,
	users.email,
	(
		SELECT money
		FROM user_accounts
		WHERE user_accounts.user_id = users.id
    ) AS account,
	(
      SELECT array_to_json(array_agg(t))
		FROM (
			SELECT CAST(id AS VARCHAR(255))
			FROM tickets
			WHERE tickets.user_id = users.id
		) t
	) AS tickets
	FROM users
) results) TO 'c:\copy\1\users.json' WITH (FORMAT text, HEADER FALSE);