ALTER TABLE station DROP CONSTRAINT station_line_id_fkey;

ALTER TABLE station
  ADD CONSTRAINT station_line_id_fkey FOREIGN KEY (line_id)
      REFERENCES line (id)
      on DELETE CASCADE;

ALTER TABLE lobby DROP CONSTRAINT lobby_station_id_fkey;

ALTER TABLE lobby
  ADD CONSTRAINT lobby_station_id_fkey FOREIGN KEY (station_id)
      REFERENCES station (id)
      on DELETE CASCADE;

ALTER TABLE im DROP CONSTRAINT im_lobby_id_fkey;

ALTER TABLE im
  ADD CONSTRAINT im_lobby_id_fkey FOREIGN KEY (lobby_id)
      REFERENCES lobby (id)
      on DELETE CASCADE;
