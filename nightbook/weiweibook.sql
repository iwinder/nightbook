-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 2017-10-26 14:17:46
-- 服务器版本： 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `weiweibook`
--

-- --------------------------------------------------------

--
-- 表的结构 `book_info`
--

CREATE TABLE `book_info` (
  `bid` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `originTitle` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `translator` varchar(255) DEFAULT NULL,
  `headImage` varchar(255) DEFAULT NULL,
  `summary` text,
  `price` decimal(10,2) DEFAULT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `pubdate` varchar(255) DEFAULT NULL,
  `average` varchar(100) DEFAULT '0.0',
  `pages` varchar(255) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `capp_info`
--

CREATE TABLE `capp_info` (
  `pid` int(11) NOT NULL,
  `appid` varchar(200) NOT NULL,
  `appsecret` varchar(255) DEFAULT NULL,
  `login_duration` int(11) DEFAULT '30',
  `session_duration` int(11) DEFAULT '2592000',
  `qcloud_appid` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'appid_qcloud',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0.0.0.0',
  `createtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `capp_info`
--

INSERT INTO `capp_info` (`pid`, `appid`, `appsecret`, `login_duration`, `session_duration`, `qcloud_appid`, `ip`, `createtime`) VALUES
(1, 'wxdbef4d039306f617', 'ac2af8f633cb7db098a59f39d3dc96ad', 30, 2592000, 'appid_qcloud', '0.0.0.0', '2017-10-08 10:08:25');

-- --------------------------------------------------------

--
-- 表的结构 `capp_session_info`
--

CREATE TABLE `capp_session_info` (
  `id` int(11) NOT NULL,
  `pid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `skey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` timestamp NOT NULL,
  `last_visit_time` timestamp NOT NULL,
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `session_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_info` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `capp_session_info`
--

INSERT INTO `capp_session_info` (`id`, `pid`, `uuid`, `skey`, `create_time`, `last_visit_time`, `open_id`, `session_key`, `user_info`) VALUES
(1, '1', '27309073-4bc7-4e7c-af5a-396fa50b0310', '63a9c13d-39c0-4302-86dc-f1aae8911898', '2017-10-25 16:06:47', '2017-10-25 16:06:47', 'odAYL0cuv-UbtXy4KIsiZFcYrdSM', 'NWrFgdDN5XKIENRLndVi/g==', 'eyJjb3VudHJ5IjoiQmVybXVkYSIsIndhdGVybWFyayI6eyJhcHBpZCI6Ind4ZGJlZjRkMDM5MzA2ZjYxNyIsInRpbWVzdGFtcCI6MTUwODk0NzYwNn0sImdlbmRlciI6MSwicHJvdmluY2UiOiIiLCJjaXR5IjoiIiwiYXZhdGFyVXJsIjoiaHR0cHM6Ly93eC5xbG9nby5jbi9tbW9wZW4vdmlfMzIvUTBqNFR3R1RmVEpQWER3N0ZIWFpmMnVkem1TT25tZkI1UW9wRFpJbURvVkpEcnRYUUpXaWNaaGliN09PN3pRYVBVbzV4UDVVWnpOR3RwcDdmSXBuN01PQS8wIiwib3BlbklkIjoib2RBWUwwY3V2LVVidFh5NEtJc2laRmNZcmRTTSIsIm5pY2tOYW1lIjoi5rGQ5p6rIiwibGFuZ3VhZ2UiOiJ6aF9DTiJ9'),
(2, '1', 'f0b7bf51-799e-4460-9861-1850e2683f80', 'baba77b0-914a-4452-92f5-d6f996bef407', '2017-10-17 13:50:08', '2017-10-17 13:50:08', 'odAYL0f7tUktScFxSZYr3l_AJCLg', 't/wft/hvL3g2SI8rqTtoVw==', 'eyJjb3VudHJ5IjoiQ2hpbmEiLCJ3YXRlcm1hcmsiOnsiYXBwaWQiOiJ3eGRiZWY0ZDAzOTMwNmY2MTciLCJ0aW1lc3RhbXAiOjE1MDgyNDgyMDd9LCJnZW5kZXIiOjEsInByb3ZpbmNlIjoiSGViZWkiLCJjaXR5IjoiU2hpamlhemh1YW5nIiwiYXZhdGFyVXJsIjoiaHR0cHM6Ly93eC5xbG9nby5jbi9tbW9wZW4vdmlfMzIvMk1BcUJTRU5LMmliZmliTlZZYXNNcTcxTVFMMVcwS0d4NFByYW5veGJRVkxlcUxlb1pic29yRTZDY2pESlZCamd1OG5zZTdFQllIS2lhRUJlbVV2YUxjdEEvMCIsIm9wZW5JZCI6Im9kQVlMMGY3dFVrdFNjRnhTWllyM2xfQUpDTGciLCJuaWNrTmFtZSI6IvCfmYQiLCJsYW5ndWFnZSI6InpoX0NOIn0=');

-- --------------------------------------------------------

--
-- 表的结构 `issues`
--

CREATE TABLE `issues` (
  `id` bigint(20) NOT NULL,
  `uid` varchar(255) DEFAULT NULL,
  `issues` text,
  `createtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `user_book`
--

CREATE TABLE `user_book` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `bid` bigint(20) DEFAULT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `readStatus` tinyint(1) DEFAULT '1',
  `deleteStatus` tinyint(1) DEFAULT '1',
  `updatetime` timestamp NULL DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `user_info`
--

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `headimg` varchar(255) DEFAULT NULL,
  `creattime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book_info`
--
ALTER TABLE `book_info`
  ADD PRIMARY KEY (`bid`),
  ADD UNIQUE KEY `isbn_index` (`isbn`);

--
-- Indexes for table `capp_info`
--
ALTER TABLE `capp_info`
  ADD PRIMARY KEY (`pid`);

--
-- Indexes for table `capp_session_info`
--
ALTER TABLE `capp_session_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `issues`
--
ALTER TABLE `issues`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_book`
--
ALTER TABLE `user_book`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `book_info`
--
ALTER TABLE `book_info`
  MODIFY `bid` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=160;
--
-- 使用表AUTO_INCREMENT `capp_info`
--
ALTER TABLE `capp_info`
  MODIFY `pid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- 使用表AUTO_INCREMENT `capp_session_info`
--
ALTER TABLE `capp_session_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- 使用表AUTO_INCREMENT `issues`
--
ALTER TABLE `issues`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- 使用表AUTO_INCREMENT `user_book`
--
ALTER TABLE `user_book`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;
--
-- 使用表AUTO_INCREMENT `user_info`
--
ALTER TABLE `user_info`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
