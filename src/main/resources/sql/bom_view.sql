SELECT product_variant.product_id,
       io.product_variant_id,
       orders.order_id,
       orders.carport_width,
       orders.carport_length,
       orders.status,
       orders.users_id,
       orders.total_price,
       io.order_item_id,
       io.quantity,
       io.description,
       product_variant.length,
       product.name,
       product.unit,
       product.price
FROM orders
         JOIN order_item io USING (order_id)
         JOIN product_variant USING (product_variant_id)
         JOIN product USING (product_id);