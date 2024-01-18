CREATE TABLE IF NOT EXISTS public.organization
(
    id                         SERIAL        NOT NULL PRIMARY KEY,
    organization_name          VARCHAR(225)  NULL,
    organization_details       VARCHAR(225)  NULL,
    organization_address       VARCHAR(225)  NULL,
    organization_phone         VARCHAR(40)   NULL,
    organization_email         VARCHAR(100)  NULL,
    tax_id                     VARCHAR(100)  NULL,
    registration_id            VARCHAR(100)  NULL,
    created_at  TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_organization_id ON organization (id);

CREATE TABLE IF NOT EXISTS public.store
(
    id                  SERIAL         NOT NULL PRIMARY KEY,
    store_name          VARCHAR(255)   NOT NULL,
    store_code          VARCHAR(50)    NULL,
    store_country       VARCHAR(100)   NULL,
    store_region        VARCHAR(50)    NULL,
    store_city          VARCHAR(50)    NULL,
    store_phone         VARCHAR(100)   NULL,
    store_email         VARCHAR(50)    NULL,
    epaper_count        VARCHAR(50)    NULL,
    gateway_count       VARCHAR(50)    NULL,
	organization_id     VARCHAR(100)   NOT NULL,
    created_at          TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_store_id ON store (id);


CREATE TABLE IF NOT EXISTS public.area
(
    id                  SERIAL         NOT NULL PRIMARY KEY,
    area_name           VARCHAR(255)   NULL,
    area_description    VARCHAR(50)    NULL,
    area_code           VARCHAR(100)   NULL,
    linked_rack         VARCHAR(50)    NULL,
    epaper_count        VARCHAR(50)    NULL,
    getway_count        VARCHAR(50)    NULL,
    store_id            VARCHAR(100)   NULL,
    created_at          TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_area_id ON area (id);

CREATE TABLE IF NOT EXISTS public.rack
(
    id                  SERIAL         NOT NULL PRIMARY KEY,
    rack_name           VARCHAR(200)   NULL,
    rack_details        VARCHAR(255)   NULL,
    rack_number         VARCHAR(50)    NULL,
    rack_area           VARCHAR(50)    NULL,
    rack_image          VARCHAR(50)    NULL,
    epaper_count        VARCHAR(50)    NULL,
    getway_count        VARCHAR(50)    NULL,
    store_id            VARCHAR(50)    NULL,
    area_id             VARCHAR(50)    NULL,
    created_at          TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_rack_id ON rack (id);

CREATE TABLE IF NOT EXISTS public.template
(
    id                      SERIAL         NOT NULL PRIMARY KEY,
    template_name           VARCHAR(255)   NULL,
    template_details        VARCHAR(225)   NULL,
    template_attribute      JSON           NULL,
    linked_product          VARCHAR(100)   NULL,
    created_at              TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_template_id ON template (id);

CREATE TABLE IF NOT EXISTS public.product
(
    id                      SERIAL         NOT NULL PRIMARY KEY,
    product_attribute       JSON           NULL,
    linked_epaper           VARCHAR(255)   NULL,
    linked_rack             VARCHAR(255)   NULL,
    created_at              TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_product_id ON product (id);


CREATE TABLE IF NOT EXISTS public.epaper
(
    id                      SERIAL        NOT NULL PRIMARY KEY,
    identity_id             VARCHAR(225)  NULL,
    product_id              VARCHAR(225)  NULL,
    linked_gateway          VARCHAR(225)  NULL,
    battery_status          VARCHAR(225)  NULL,
    process_status          VARCHAR(255)  NULL,
    network_status          VARCHAR(255)  NULL,
    signal_strength         VARCHAR(255)  NULL,
    is_removed              BOOLEAN       NULL,
    label_technology        VARCHAR(225)  NULL,
    removed_at              TIMESTAMPTZ   NULL,
    started_at              TIMESTAMPTZ   NULL,
    completed_at            TIMESTAMPTZ   NULL,
    created_at              TIMESTAMPTZ   NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ   NULL DEFAULT NOW()
);
CREATE INDEX index_epaper_id ON epaper (id);



CREATE TABLE IF NOT EXISTS public.users
(
    id                       SERIAL        NOT NULL PRIMARY KEY,
    user_fname               VARCHAR(150)  NULL,
    user_lname               VARCHAR(150)  NULL,
    user_email               VARCHAR(150)  NOT NULL,
    user_pass                VARCHAR(100)  NOT NULL,
    user_salt                VARCHAR(200)  NULL,
    user_mobile              VARCHAR(50)   NULL,
    user_roles               TEXT          NULL,
    is_Active                BOOLEAN       NULL,
    is_verified              BOOLEAN       NULL,
    valid_until              TIMESTAMPTZ   NULL,
    last_login               TIMESTAMPTZ   NULL,
    password_requested_at    TIMESTAMPTZ   NULL,
    created_at               TIMESTAMPTZ   NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NULL DEFAULT NOW()
);
CREATE INDEX index_user_id ON users (id);


CREATE TABLE IF NOT EXISTS public.user_role
(
    id                       SERIAL        NOT NULL PRIMARY KEY,
    role_name                VARCHAR(150)  NULL,
    created_at               TIMESTAMPTZ   NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NULL DEFAULT NOW()
);
CREATE INDEX index_role_id ON user_role (id);

CREATE TABLE IF NOT EXISTS public.role_permission
(
    id                       SERIAL        NOT NULL PRIMARY KEY,
    role_id                                INTEGER  NULL,
    created_at               TIMESTAMPTZ   NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NULL DEFAULT NOW()
);
CREATE INDEX index_role_permission_id ON role_permission (id);

CREATE TABLE IF NOT EXISTS public.user_permission
(
    id                       SERIAL        NOT NULL PRIMARY KEY,
    permission_name          VARCHAR(150)  NOT NULL,
    created_at               TIMESTAMPTZ   NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NULL DEFAULT NOW()
);
CREATE INDEX index_user_permission_id ON user_permission (id);