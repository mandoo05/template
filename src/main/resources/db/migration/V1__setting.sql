CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE OR REPLACE FUNCTION uuid_v7()
RETURNS uuid AS $$
DECLARE
  unix_ts_ms BIGINT;
ts BYTEA;
rand BYTEA;
uuid_bytes BYTEA;
BEGIN
    -- 1) 현재 timestamp (밀리초 단위)
    unix_ts_ms := (EXTRACT(EPOCH FROM clock_timestamp()) * 1000)::BIGINT;

-- timestamp → 48비트만 사용
ts := set_byte(set_byte(set_byte(set_byte(set_byte(set_byte(
          E'\\000\\000\\000\\000\\000\\000'::bytea,
          0, ((unix_ts_ms >> 40) & 255)::int),
          1, ((unix_ts_ms >> 32) & 255)::int),
          2, ((unix_ts_ms >> 24) & 255)::int),
          3, ((unix_ts_ms >> 16) & 255)::int),
          4, ((unix_ts_ms >> 8) & 255)::int),
          5, (unix_ts_ms & 255)::int);

    -- 2) 랜덤 10바이트
rand := gen_random_bytes(10);

    -- 3) UUID 16바이트 조합
uuid_bytes := ts || rand;

    -- version = 7 (0111)
uuid_bytes := set_byte(uuid_bytes, 6,
          (get_byte(uuid_bytes, 6) & 0x0F) | 0x70);

    -- variant = RFC4122 (10xxxxxx)
uuid_bytes := set_byte(uuid_bytes, 8,
          (get_byte(uuid_bytes, 8) & 0x3F) | 0x80);

RETURN encode(uuid_bytes, 'hex')::uuid;
END;
$$ LANGUAGE plpgsql VOLATILE;
