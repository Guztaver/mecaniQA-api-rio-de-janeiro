-- mecaniQA | OAT 3 | MySQL 8+
-- Execute este arquivo uma vez para criar o esquema e os dados iniciais.

CREATE DATABASE IF NOT EXISTS mecaniqa
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE mecaniqa;

CREATE TABLE IF NOT EXISTS pecas (
  codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
  codigo_barras VARCHAR(100) UNIQUE,
  fornecedor VARCHAR(150),
  quantidade_estoque INT NOT NULL DEFAULT 0,
  preco_custo DECIMAL(12,2),
  preco_venda DECIMAL(12,2) NOT NULL,
  tamanho VARCHAR(50),
  cor VARCHAR(50),
  categoria VARCHAR(30),
  data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS servicos (
  codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(150) NOT NULL,
  tempo_estimado_minutos INT NOT NULL,
  custo_tabelado DECIMAL(12,2) NOT NULL,
  data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pedidos_pecas (
  codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
  descricao VARCHAR(500),
  status VARCHAR(30) NOT NULL,
  valor_total DECIMAL(12,2) NOT NULL DEFAULT 0,
  data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS itens_pedido_pecas (
  codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
  pedido_pecas_codigo BIGINT NOT NULL,
  peca_codigo BIGINT NOT NULL,
  quantidade INT NOT NULL,
  preco_unitario DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_pecas_codigo) REFERENCES pedidos_pecas(codigo) ON DELETE CASCADE,
  CONSTRAINT fk_item_peca FOREIGN KEY (peca_codigo) REFERENCES pecas(codigo)
);

CREATE TABLE IF NOT EXISTS ordens_servico (
  codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
  descricao VARCHAR(500),
  cliente VARCHAR(150) NOT NULL,
  veiculo VARCHAR(150) NOT NULL,
  status VARCHAR(30) NOT NULL,
  pedido_pecas_codigo BIGINT,
  valor_total DECIMAL(12,2) NOT NULL DEFAULT 0,
  data_abertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  data_finalizacao DATETIME NULL,
  CONSTRAINT fk_os_pedido FOREIGN KEY (pedido_pecas_codigo) REFERENCES pedidos_pecas(codigo)
);

CREATE TABLE IF NOT EXISTS ordem_servico_servicos (
  ordem_servico_codigo BIGINT NOT NULL,
  servico_codigo BIGINT NOT NULL,
  PRIMARY KEY (ordem_servico_codigo, servico_codigo),
  CONSTRAINT fk_os_servico_ordem FOREIGN KEY (ordem_servico_codigo) REFERENCES ordens_servico(codigo) ON DELETE CASCADE,
  CONSTRAINT fk_os_servico_servico FOREIGN KEY (servico_codigo) REFERENCES servicos(codigo)
);

INSERT INTO pecas (codigo_barras, fornecedor, quantidade_estoque, preco_custo, preco_venda, tamanho, cor, categoria)
SELECT '789100000001', 'Auto Peças Rio', 10, 25.00, 35.00, 'Padrão', 'Prata', 'MOTOR'
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE codigo_barras = '789100000001');

INSERT INTO servicos (nome, tempo_estimado_minutos, custo_tabelado)
SELECT 'Troca de óleo', 45, 150.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Troca de óleo');
