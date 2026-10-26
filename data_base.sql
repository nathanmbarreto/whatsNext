-- =====================================================
-- WHAT'S NEXT? - BANCO DE DADOS
-- PostgreSQL
-- =====================================================

-- 1. USUARIO
CREATE TABLE usuario (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(254) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- 2. MEDIA
-- Filmes, séries e jogos
CREATE TABLE media (
    media_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    external_id BIGINT NOT NULL,
    source VARCHAR(10) NOT NULL,
    media_type VARCHAR(10) NOT NULL,

    media_name VARCHAR(255) NOT NULL,
    background_image TEXT,
    released DATE,

    rating NUMERIC(4,2),
    rating_scale NUMERIC(3,1),

    description_original TEXT,
    description_pt_br TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_synced_at TIMESTAMPTZ,

    CONSTRAINT chk_media_source
        CHECK (source IN ('RAWG', 'TMDB')),

    CONSTRAINT chk_media_type
        CHECK (media_type IN ('GAME', 'MOVIE', 'SERIES')),

    CONSTRAINT chk_media_source_type
        CHECK (
            (source = 'RAWG' AND media_type = 'GAME')
            OR
            (source = 'TMDB' AND media_type IN ('MOVIE', 'SERIES'))
        ),

    CONSTRAINT chk_media_rating
        CHECK (
            rating IS NULL
            OR (
                rating >= 0
                AND rating_scale IS NOT NULL
                AND rating_scale > 0
                AND rating <= rating_scale
            )
        ),

    CONSTRAINT uq_media_external
        UNIQUE (source, media_type, external_id)
);


-- 3. GENEROS
CREATE TABLE genre (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);


-- 4. RELACIONAMENTO MEDIA x GENEROS
CREATE TABLE media_genre (
    media_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,

    PRIMARY KEY (media_id, genre_id),

    CONSTRAINT fk_media_genre_media
        FOREIGN KEY (media_id)
        REFERENCES media(media_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_media_genre_genre
        FOREIGN KEY (genre_id)
        REFERENCES genre(genre_id)
        ON DELETE CASCADE
);


-- 5. COMENTARIOS
CREATE TABLE comentarios (
    id_comentario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    comentario TEXT NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_comentario_tamanho
        CHECK (
            CHAR_LENGTH(TRIM(comentario)) BETWEEN 1 AND 500
        ),

    CONSTRAINT fk_comentario_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_comentario_media
        FOREIGN KEY (media_id)
        REFERENCES media(media_id)
        ON DELETE CASCADE
);


-- 6. AVALIACOES
-- Cada usuário pode avaliar cada mídia uma única vez.
CREATE TABLE avaliacao (
    usuario_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    nota NUMERIC(3,1) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_avaliacao
        PRIMARY KEY (usuario_id, media_id),

    CONSTRAINT chk_avaliacao_nota
        CHECK (nota >= 0 AND nota <= 10),

    CONSTRAINT fk_avaliacao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_avaliacao_media
        FOREIGN KEY (media_id)
        REFERENCES media(media_id)
        ON DELETE CASCADE
);


-- 7. FAVORITOS
-- Cada usuário pode favoritar cada mídia uma única vez.
CREATE TABLE favoritos (
    usuario_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_favoritos
        PRIMARY KEY (usuario_id, media_id),

    CONSTRAINT fk_favoritos_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_favoritos_media
        FOREIGN KEY (media_id)
        REFERENCES media(media_id)
        ON DELETE CASCADE
);


-- =====================================================
-- INDICES PARA CONSULTAS MAIS FREQUENTES
-- =====================================================

CREATE INDEX idx_media_name
    ON media(media_name);

CREATE INDEX idx_media_type
    ON media(media_type);

CREATE INDEX idx_media_released
    ON media(released);

CREATE INDEX idx_comentarios_media
    ON comentarios(media_id);

CREATE INDEX idx_comentarios_usuario
    ON comentarios(usuario_id);

CREATE INDEX idx_avaliacao_media
    ON avaliacao(media_id);

CREATE INDEX idx_favoritos_media
    ON favoritos(media_id);

CREATE INDEX idx_media_genre_genre
    ON media_genre(genre_id);