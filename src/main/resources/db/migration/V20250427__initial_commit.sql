CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    cardnumber BIGINT(16) NOT NULL UNIQUE,
    fraudscore INT DEFAULT 0
);

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    userid BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(255) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id)
);

CREATE TABLE fraud_rules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    conditions VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL
);

CREATE TABLE fraud_alerts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transationid BIGINT NOT NULL,
    rule_violated BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transationid) REFERENCES transactions(id),
    FOREIGN KEY (rule_violated) REFERENCES fraud_rules(id)
);

INSERT INTO fraud_rules (name, conditions, action)
VALUES 
('Velocity Check', 'count(transactions) > 5 within 10 minutes', 'ALERT'),
('Amount Threshold', 'transaction.amount > 1000.00', 'REVIEW'),
('Card Testing Pattern', 'count(transactions) > 3 AND avg(transaction.amount) < 5.00 within 1 hour', 'BLOCK'),
('Transaction Timing', 'transaction.hour >= 23 OR transaction.hour <= 4', 'ALERT'),
('Multiple Failed Attempts', 'count(failed_transactions) >= 3 within 30 minutes', 'BLOCK'),
('New Account High Value', 'user.created_at < 7 days AND transaction.amount > 500.00', 'REVIEW'),
('Dormant Account Activity', 'last_transaction > 6 months ago', 'REVIEW'),
('Fraud Score Threshold', 'user.fraudscore > 70', 'BLOCK');