DELETE FROM active_user_profile CASCADE;
DELETE FROM active_user CASCADE;
DELETE FROM pending_user_profile CASCADE;
DELETE FROM pending_user CASCADE;
DELETE FROM group_member CASCADE;
DELETE FROM group_type CASCADE;
DELETE FROM user_type CASCADE;
DELETE FROM flyway_schema_history CASCADE;