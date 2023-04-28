# Awesome online store

## System description
- JDK 17
- Spring Boot 3.0.0
- Mysql 8.0.31

## Project topology
![Project topology](project_topology.png)
- One database is OLTP database (Online Transaction Processing System) and another database is DW (Data Warehouse – DSS).
- Web application is interfaced with OLTP database.
- Reporting tool / Analytics tool is interfaced with DW database.
- There is ETL (Extract, Transform, and Load) database code to load data from OLTP database, transform it matching DW schema design, and then loading it to DW database.
- There is CDC approach (Change Data Capture) for ETL database routine to implement incremental ETL
- Use advance database features e.g. External tables, Partitioned Tables, function base indexes, PL/SQL(procedures, functions, packages, triggers, data masking).
- The online application has robust security features, validations, user self-services (password management by users, security questions & validations).
- The DW systems use data visualization tool, such as Tableau, ClickView.
