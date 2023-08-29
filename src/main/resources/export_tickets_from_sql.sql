COPY (SELECT row_to_json(results)
FROM (
  SELECT CAST(tickets.id AS VARCHAR(255)) as _id,
  CAST(tickets.event_id AS VARCHAR(255)) tickets.event_id,
  CAST(tickets.user_id AS VARCHAR(255)) tickets.user_id,tickets.place,tickets.category
  FROM tickets
) results) TO 'c:\copy\1\tickets.json' WITH (FORMAT text, HEADER FALSE);