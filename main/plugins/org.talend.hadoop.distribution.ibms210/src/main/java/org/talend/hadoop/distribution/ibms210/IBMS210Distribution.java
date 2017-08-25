// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.hadoop.distribution.ibms210;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.talend.hadoop.distribution.AbstractDistribution;
import org.talend.hadoop.distribution.ComponentType;
import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.EHadoopVersion;
import org.talend.hadoop.distribution.ESparkVersion;
import org.talend.hadoop.distribution.NodeComponentTypeBean;
import org.talend.hadoop.distribution.component.HDFSComponent;
import org.talend.hadoop.distribution.component.SparkBatchComponent;
import org.talend.hadoop.distribution.component.SparkStreamingComponent;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.constants.SparkBatchConstant;
import org.talend.hadoop.distribution.constants.SparkStreamingConstant;
import org.talend.hadoop.distribution.ibms210.modulegroup.IBMS210HDFSModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.IBMS210SparkBatchModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.IBMS210SparkStreamingModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkbatch.IBMS210SparkBatchParquetNodeModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkbatch.IBMS210SparkBatchS3NodeModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingFlumeNodeModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingKafkaAssemblyModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingKafkaAvroModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingKafkaClientModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingKinesisNodeModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingParquetNodeModuleGroup;
import org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkstreaming.IBMS210SparkStreamingS3NodeModuleGroup;

public class IBMS210Distribution extends AbstractDistribution implements
		HDFSComponent, SparkBatchComponent, SparkStreamingComponent {

	public static final String DISTRIBUTION_NAME = "IBMSPECTRUM";

	public static final String DISTRIBUTION_DISPLAY_NAME = "IBM Spectrum";

	public final static String VERSION = "IBMS_2_1";

	public static final String VERSION_DISPLAY = "IBM Spectrum 2.1 (Spark 1.6 only)";

	private static Map<ComponentType, Set<DistributionModuleGroup>> moduleGroups;

	private static Map<NodeComponentTypeBean, Set<DistributionModuleGroup>> nodeModuleGroups;

	private static Map<ComponentType, ComponentCondition> displayConditions;

	public IBMS210Distribution() {
		// Used to add a module group import for the components that have a
		// HADOOP_DISTRIBUTION parameter, aka. the
		// components that have the distribution list.
		moduleGroups = new HashMap<>();
		moduleGroups.put(ComponentType.HDFS,
				IBMS210HDFSModuleGroup.getModuleGroups());
		moduleGroups.put(ComponentType.SPARKBATCH,
				IBMS210SparkBatchModuleGroup.getModuleGroups());
		moduleGroups.put(ComponentType.SPARKSTREAMING,
				IBMS210SparkStreamingModuleGroup.getModuleGroups());

		// Used to add a module group import for a specific node. The given node
		// must have a HADOOP_LIBRARIES parameter.
		nodeModuleGroups = new HashMap<>();

		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKBATCH,
				SparkBatchConstant.PARQUET_INPUT_COMPONENT),
				IBMS210SparkBatchParquetNodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKBATCH,
				SparkBatchConstant.PARQUET_OUTPUT_COMPONENT),
				IBMS210SparkBatchParquetNodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKBATCH,
				SparkBatchConstant.S3_CONFIGURATION_COMPONENT),
				IBMS210SparkBatchS3NodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_INPUT_COMPONENT),
				IBMS210SparkStreamingParquetNodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_OUTPUT_COMPONENT),
				IBMS210SparkStreamingParquetNodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_STREAM_INPUT_COMPONENT),
				IBMS210SparkStreamingParquetNodeModuleGroup.getModuleGroups());
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.S3_CONFIGURATION_COMPONENT),
				IBMS210SparkStreamingS3NodeModuleGroup.getModuleGroups());

		Set<DistributionModuleGroup> kinesisNodeModuleGroups = IBMS210SparkStreamingKinesisNodeModuleGroup
				.getModuleGroups();
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_INPUT_COMPONENT),
				kinesisNodeModuleGroups);
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_INPUT_AVRO_COMPONENT),
				kinesisNodeModuleGroups);
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_OUTPUT_COMPONENT),
				kinesisNodeModuleGroups);

		Set<DistributionModuleGroup> flumeNodeModuleGroups = IBMS210SparkStreamingFlumeNodeModuleGroup
				.getModuleGroups();
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.FLUME_INPUT_COMPONENT),
				flumeNodeModuleGroups);
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.FLUME_OUTPUT_COMPONENT),
				flumeNodeModuleGroups);

		Set<DistributionModuleGroup> kafkaAssemblyModuleGroups = IBMS210SparkStreamingKafkaAssemblyModuleGroup
				.getModuleGroups();
		Set<DistributionModuleGroup> kafkaAvroModuleGroups = IBMS210SparkStreamingKafkaAvroModuleGroup
				.getModuleGroups();
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_INPUT_COMPONENT),
				kafkaAssemblyModuleGroups);
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_AVRO_INPUT_COMPONENT),
				kafkaAvroModuleGroups);
		nodeModuleGroups.put(new NodeComponentTypeBean(
				ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_OUTPUT_COMPONENT),
				IBMS210SparkStreamingKafkaClientModuleGroup.getModuleGroups());

		// Used to hide the distribution according to other parameters in the
		// component.
		displayConditions = new HashMap<>();
	}

	@Override
	public String getDistribution() {
		return DISTRIBUTION_NAME;
	}

	@Override
	public String getDistributionName() {
		return DISTRIBUTION_DISPLAY_NAME;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public String getVersionName(ComponentType componentType) {
		return VERSION_DISPLAY;
	}

	@Override
	public EHadoopVersion getHadoopVersion() {
		return EHadoopVersion.HADOOP_2;
	}

	@Override
	public boolean doSupportKerberos() {
		return true;
	}

	@Override
	public Set<DistributionModuleGroup> getModuleGroups(
			ComponentType componentType) {
		return moduleGroups.get(componentType);
	}

	@Override
	public Set<DistributionModuleGroup> getModuleGroups(
			ComponentType componentType, String componentName) {
		return nodeModuleGroups.get(new NodeComponentTypeBean(componentType,
				componentName));
	}

	@Override
	public ComponentCondition getDisplayCondition(ComponentType componentType) {
		return displayConditions.get(componentType);
	}

	@Override
	public boolean doSupportCrossPlatformSubmission() {
		return true;
	}

	@Override
	public boolean doSupportUseDatanodeHostname() {
		return true;
	}

	@Override
	public boolean doSupportSequenceFileShortType() {
		return true;
	}

	@Override
	public boolean doSupportEmbeddedMode() {
		return false;
	}

	@Override
	public boolean doSupportStandaloneMode() {
		return true;
	}

	@Override
    public Set<ESparkVersion> getSparkVersions() {
        Set<ESparkVersion> version = new HashSet<>();
        version.add(ESparkVersion.SPARK_1_6);
        return version;
	}

	@Override
	public boolean doSupportDynamicMemoryAllocation() {
		return true;
	}

	@Override
	public boolean isExecutedThroughSparkJobServer() {
		return false;
	}

	@Override
	public boolean doSupportCheckpointing() {
		return true;
	}

	@Override
	public boolean doSupportSparkStandaloneMode() {
		return true;
	}

	@Override
	public boolean doSupportSparkYarnClientMode() {
		return false;
	}

	@Override
	public boolean doSupportOldImportMode() {
		return false;
	}

	@Override
	public boolean doSupportBackpressure() {
		return true;
	}

	@Override
	public boolean doSupportSSLwithKerberos() {
		return true;
	}

	@Override
	public boolean doSupportImpersonation() {
		// TODO Auto-generated method stub
		return false;
	}

}
