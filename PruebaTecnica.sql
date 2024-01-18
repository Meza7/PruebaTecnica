USE PracticaSiman
GO

CREATE TABLE olistCustomers(
customerId NVARCHAR(50) PRIMARY KEY NOT NULL,
uniqueCustomerId NVARCHAR(50) NOT NULL,
customerZipCode NVARCHAR(10),
customerCity NVARCHAR(25),
customerState NVARCHAR(5)
)

CREATE TABLE olistOrders(
orderId NVARCHAR(50) PRIMARY KEY NOT NULL,
customerId NVARCHAR(50) NOT NULL,
orderStatus NVARCHAR(20),
orderPurchaseTime DATETIME,
orderApprovedAt DATETIME,
orderDeliveredCarrierDate DATETIME,
orderDeliveredCustomerDate DATETIME,
OrderEstimatedDeliveryDate DATETIME

FOREIGN KEY (customerId) REFERENCES olistCustomers(customerId)
)

CREATE TABLE olistOrderItems(
orderId NVARCHAR(50) NOT NULL,
orderItemId NVARCHAR(50) NOT NULL,
productId NVARCHAR(50) NOT NULL,
sellerId NVARCHAR(50) NOT NULL,
shippingLimitDate DATETIME,
price DECIMAL,
freightValue DECIMAL

PRIMARY KEY (orderId, orderItemId)
FOREIGN KEY (orderId) REFERENCES olistOrders(orderId)
)

CREATE TABLE olistOrderItemsETL(
orderId NVARCHAR(50) NOT NULL,
orderItemId SMALLINT NOT NULL,
productId NVARCHAR(50) NOT NULL,
sellerId NVARCHAR(50) NOT NULL,
shippingLimitDate DATETIME,
price DECIMAL,
freightValue DECIMAL

PRIMARY KEY (orderId, orderItemId)
)

SELECT c.customerId, o.OrderStatus, o.orderPurchaseTime, o.orderEstimatedDeliveryDate
FROM olistCustomers c
JOIN olistOrders o ON c.customerId = o.customerId

SELECT o.orderId, i.productId, i.price
FROM olistOrders o
JOIN olistOrderItems i ON o.orderId = i.orderId

SELECT MAX(price) AS MaxPrice
FROM olistOrderItems
GO

CREATE PROCEDURE TransformLoadData
AS
BEGIN
    
    -- Cast and insert data
    INSERT INTO olistOrderItems(orderItemId)
    SELECT CAST(orderItemId AS NVARCHAR(255))
    FROM olistOrderItemsETL;
END;