# Pharmacy Warehouse Management Application

## Course
**Software Engineering**

## Team
- Michail Marakis  
- Prodromos-Aris Makarounas  

---

## Introduction

This application simplifies the medicine ordering process from the perspective of pharmacists who are registered and use the platform.  
At the same time, it significantly supports pharmacy warehouse administrators by allowing them to serve their clients (pharmacists) efficiently and quickly, offering rich functionality through a centralized system.

The goal of the application is to automate ordering, inventory management, and backorder handling in a reliable and structured way.

---

## Stakeholders
- Customers (Pharmacists)
- Pharmacy Warehouse Administrator

---

## Pharmacy Warehouse Features

- The warehouse administrator can view a list of **pending orders**.
- Any order can be selected for execution, automatically changing its status from **“Pending”** to **“Completed”**, while updating warehouse inventory.
- After completion, orders are **packed and shipped** to the corresponding pharmacy.
- Orders can be **cancelled** if a product is unavailable, freeing reserved items and automatically serving existing **backorders**.
- When **new product batches** are received, the administrator updates inventory and relevant backorders are activated automatically.
- The administrator can **withdraw defective or dangerous medicines** from the market and notify all pharmacies that received them (**traceability support**).
- The system provides **sales statistics**, including:
  - Order history  
  - Revenue per customer  
  - Product sales per time period  

---

## Pharmacist Features

- Pharmacists can **register** by providing:
  - First and last name  
  - Phone number  
  - Email  
  - Password and confirmation  
  - Address, region, postal code  
  - Tax Identification Number (VAT)
- They can **browse products**, which include:
  - Product name  
  - Price (without and with VAT)  
  - Stock availability  
  - Medicine category  
  - EOF product code
- Products can be added to a **shopping cart**, allowing:
  - Immediate order submission  
  - Temporary saving for later submission
- If a product is **out of stock**, a warning is shown before order submission.
- When an order includes unavailable products:
  - Available items are shipped  
  - Unavailable items are placed in **backorder**
- **Backorders** remain pending and reserve products until stock is replenished.
- Backorders are processed using **First-Come First-Serve (FCFS)** priority.
- When stock becomes available again, pharmacists are **notified via email**.

---

## Summary

The application improves the ordering and fulfillment process between pharmacies and pharmacy warehouses by offering automation, transparency, and efficient inventory management.
