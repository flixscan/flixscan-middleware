CREATE TABLE IF NOT EXISTS public.organization
(
    organization_id            BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    organization_name          VARCHAR(225)  NOT NULL,
    organization_address       VARCHAR(225)  NOT NULL,
    organization_phone         VARCHAR(20)   NOT NULL,
    organization_email         VARCHAR(80)   NOT NULL,
    created_at                 TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_organization PRIMARY KEY (organization_id)
);


CREATE TABLE IF NOT EXISTS public.stores
(
    id                  SERIAL        NOT NULL PRIMARY KEY,
    organization_id     VARCHAR(100)   NOT NULL,
    store_name          VARCHAR(255)   NOT NULL,
    store_code          VARCHAR(50)    NULL,
    store_country       VARCHAR(100)   NULL,
    store_region        VARCHAR(50)    NULL,
    store_city          VARCHAR(50)    NULL,
    store_phone         VARCHAR(100)   NULL,
    store_email         VARCHAR(50)    NULL,
    epaper_count        VARCHAR(50)    NULL,
    gateway_count       VARCHAR(50)    NULL,
    created_at          TIMESTAMPTZ    NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NULL DEFAULT NOW()
);
CREATE INDEX index_store_id ON stores (id);

CREATE TABLE IF NOT EXISTS public.racks
(
    rack_id             BIGINT         NOT NULL GENERATED ALWAYS AS IDENTITY,
    store_id            VARCHAR(50)    NOT NULL,
    rack_name           VARCHAR(200)   NOT NULL,
    rack_details        VARCHAR(255)   NOT NULL,
    rack_number         VARCHAR(50)    NOT NULL,
    rack_area           VARCHAR(50)    NOT NULL,
    rack_image          VARCHAR(50)    NOT NULL,
    epaper_count        VARCHAR(50)    NOT NULL,
    getway_count        VARCHAR(50)    NOT NULL,
    created_at          TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_racks PRIMARY KEY (rack_id)
);
CREATE INDEX index_rack_id ON racks (rack_id);

CREATE TABLE IF NOT EXISTS public.products
(
    product_id            BIGINT         NOT NULL GENERATED ALWAYS AS IDENTITY,
    rack_id               VARCHAR(50)    NOT NULL,
    epaper_id             VARCHAR(225)   NOT NULL,
    product_attribute     JSON           NOT NULL,
    created_at            TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_product PRIMARY KEY (product_id)
);
CREATE INDEX index_product_id ON products (product_id);


CREATE TABLE IF NOT EXISTS public.epapers
(
    epaper_id               BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_id              VARCHAR(225)  NOT NULL,
    linked_gateway          VARCHAR(225)  NOT NULL,
    battery_status          VARCHAR(225)  NOT NULL,
    process_status          VARCHAR(100)  NOT NULL,
    network_status          VARCHAR(100)  NOT NULL,
    signal_strength         VARCHAR(100)  NOT NULL,
    is_removed              BOOLEAN       NOT NULL,
    label_technology        VARCHAR(225)  NOT NULL,
    removed_at              TIMESTAMPTZ   NULL,
    started_at              TIMESTAMPTZ   NULL,
    completed_at            TIMESTAMPTZ   NULL,
    created_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_epaper    PRIMARY KEY (epaper_id)
);
CREATE INDEX index_epaper_id ON epapers (epaper_id);

CREATE TABLE IF NOT EXISTS public.templates
(
    template_id             BIGINT         NOT NULL GENERATED ALWAYS AS IDENTITY,
    rack_id                 VARCHAR(50)    NOT NULL,
    epaper_id               VARCHAR(225)   NOT NULL,
    template_details        JSON           NOT NULL,
    created_at              TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_templates PRIMARY KEY (template_id)
);
CREATE INDEX index_template_id ON templates (template_id);

CREATE TABLE IF NOT EXISTS public.users
(
    user_id                  BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_name                VARCHAR(150)  NOT NULL,
    user_email               VARCHAR(150)  NOT NULL,
    user_pass                VARCHAR(100)  NOT NULL,
    user_salt                VARCHAR(200)  NULL,
    user_mobile              VARCHAR(50)   NULL,
    user_roles               TEXT          NOT NULL,
    password_requested_at    TIMESTAMPTZ   NULL,
    valid_until              TIMESTAMPTZ   NULL,
    last_login               TIMESTAMPTZ   NULL,
    created_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_users PRIMARY KEY  (user_id),
    CONSTRAINT uk_users_email UNIQUE (user_email)
);
CREATE INDEX index_user_id_user ON users (user_id, user_email);


CREATE TABLE IF NOT EXISTS public.user_roles
(
    role_id                  BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    role_name                VARCHAR(150)  NOT NULL,
    created_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id)
);
CREATE INDEX index_role_id_user_roles ON user_roles (role_id);

CREATE TABLE IF NOT EXISTS public.role_permission
(
    permission_id                          INTEGER  NOT NULL,
    role_id                                INTEGER  NOT NULL,
    created_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_role_permission          PRIMARY  KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS public.user_permissions
(
    permission_id            BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    permission_name          VARCHAR(150)  NOT NULL,
    created_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_user_permission PRIMARY KEY (permission_id)
);
