rSmart Solr Changelog
=====================

org.sakaiproject.nakamura.solr-1.4.2.1-rsmart
---------------------------------------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.4.2.1-rsmart d22366e
* NOJIRA rev minor version of artifact id e60ed17
* Squashed commit of the following: bd197ae

org.sakaiproject.nakamura.solr-1.4.2
------------------------------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.4.2 23dfa59
* [maven-release-plugin] prepare for next development iteration 4de7c9a

org.sakaiproject.nakamura.solr-1.4.1
------------------------------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.4.1 12a2744
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.4 9f4e5be
* Adjust maven bundle plugin version to match core. 991715e
* Folow up to merge #95 Merge 95 would have been find for a single instance app server but with a backoff of 100ms there was little chance a
* Fixed #93 dc90dfb
* Fixed version of commons-io 07d18b2
* Fixed #92, fortunately no code changes were required. 7e7dd79
* Added key sizing tests for a bloom filter. ed51c11
* [maven-release-plugin] prepare for next development iteration 7f49a71

1.3.3-rsmart
------------
* [maven-release-plugin] prepare release 1.3.3-rsmart cb32dff
* KERN-2669 Remove solr rollback. (cherry picked from commit b3aa310099b42a37458c8788c544cb11b834fc29) 5e57e17
* [maven-release-plugin] prepare for next development iteration 48244bf

1.3.2-rsmart
------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.3.2-rsmart-SNAPSHOT d651930
* ACAD-789 add field for external course id for basic sis 0c3832d
* NOJIRA comment out sonatype oss parent b51cdc8
* NOJIRA comment out gpg plugin e2dc77c
* NOJIRA switch to SNAPSHOT version e722f21
* NOJIRA prepare for maven release plugin 6f61a1e
* NOJIRA rev version number for next build 15f3cf8
* ACAD-145 fixed some merge isses and case insensitive email addresses d31dd9f
* ACAD-707 created rsmart version # instead of relying on groupId 37bc24d

acad-1.3.1
----------
* ACAD-623 cherry-picked ets-berekely-edu commit 0f638ef7 (by Ray Davis) as workaround for KERN-2516 to enable 1.1 migration & rev'd solr ve
* NOJIRA rev groupId and version for cherry pick of solr reindexing fixes a983c43

org.sakaiproject.nakamura.solr-1.3
----------------------------------
* [maven-release-plugin] prepare release  org.sakaiproject.nakamura.solr-1.3 a3f00da
* Fixed #85 209a8be
* fixed #86 Made the API documentation more explicit about what to do in the case of no action. Put null checks in place in all locations wh
* Addressed issue #84 Code was confusing There was a bug in an event with a ttl too low was going into the slowest queue rather than the fas
* Fire a org/sakaiproject/nakamura/solr/SOFT_COMMIT event when a soft commit is performed. 43a4d7b
* Fixed #81 c17aa26
* Added fields that are indexed in solr now that were being indexed only in sparse. 4c8322c
* fixed #79, pity it missed the release. 766b86b
* [maven-release-plugin] prepare for next development iteration 2a61a86

org.sakaiproject.nakamura.solr-1.2
----------------------------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.2 b29d449
* Fixed naming confusion with the SolrClient types 6e41893
* KERN-2273 add showalways field to satisfy search requirement 3477679
* KERN-2179 for multi-page indexing, we need content to be a multi-value field ce7457f
* Updated creation of update servers to ensure that remote failure propagates correctly. 4b1faa6
* fixed #77 376f172
* Embedded http client to avoid having the bundle raw with other httpclient bundles. 008ec62
* Added more unit test coverage. 5a2f8ee
* Fixed #75 The JCR specific indexers are removed by this patch. Its still possible to index JCR you can add the indexers back in using a se
* Fixed #76 Added QoSIndexHandler 58a87e9
* [maven-release-plugin] prepare for next development iteration cad0473

org.sakaiproject.nakamura.solr-1.1
----------------------------------
* [maven-release-plugin] prepare release org.sakaiproject.nakamura.solr-1.1 f0b6a71
* Added some debug and made the queue start up a bit safer. bd75208
* Dropped reliance on Sling common jar. 192a526
* Fixed Soak test, too many ttls 6a28371
* Fixed #72 Fixed #73 see issues for details. 2538ad9
* Rebound to core 1.2 release 9ebcb39
* KERN-1938 upgrade two more classes to Google guava. Switch nakamura.core version to 1.2-SNAPSHOT 0148525
* KERN-1938 apply Aleksander's patch to upgrade to guava r09 95c252c
* fixed #68 5dd90d2
* fixed #69 You can now configure the driver and select it from the WebConsole without having to rebuild the jar. ad28ac2
* Fixed #68 a8b17ac
* Binding to the 1.1-SNAPSHOT release of core. 3d9774a
* Upgraded Solr to latest version. ccc4792
* Added sakai repo back in to get the solr snapshots, pending solr upgrade. 54e1e41
* adjusted repos d26bdfa
* Fixing build. 1a064aa
* [maven-release-plugin] prepare for next development iteration 4f37a96

org.sakaiproject.nakamura.solr-1.0.2
------------------------------------
* changing version number for 1.0.2 release 2d8302d
* KERN-2222 ab5e792

org.sakaiproject.nakamura.solr-1.0.1
------------------------------------
* KERN-2179 for multi-page indexing, we need content to be a multi-value field 6a65013

org.sakaiproject.nakamura.solr-1.0
----------------------------------
