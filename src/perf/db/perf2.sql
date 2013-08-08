-- 
-- ***** BEGIN LICENSE BLOCK *****
-- Zimbra Collaboration Suite Server
-- Copyright (C) 2012, 2013 Zimbra Software, LLC.
-- 
-- The contents of this file are subject to the Zimbra Public License
-- Version 1.3 ("License"); you may not use this file except in
-- compliance with the License.  You may obtain a copy of the License at
-- http://www.zimbra.com/license.
-- 
-- Software distributed under the License is distributed on an "AS IS"
-- basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
-- ***** END LICENSE BLOCK *****
-- 
create table perf2 (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 created TIMESTAMP(8),
 name VARCHAR(35),
 appid INT,
 buildid INT,
 browserid INT,
 clientid INT,
 milestoneid INT,
 start BIGINT,
 launched BIGINT,
 loaded BIGINT,
 delta BIGINT,
 delta_internal BIGINT,
 messageid INT
 );

