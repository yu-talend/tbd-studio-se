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

package org.talend.hadoop.distribution.ibms220;

import java.util.HashMap;
import java.util.List;
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
import org.talend.hadoop.distribution.ibms220.modulegroup.IBMS220HDFSModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.IBMS220SparkBatchModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.IBMS220SparkStreamingModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkbatch.IBMS220GraphFramesNodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkbatch.IBMS220SparkBatchParquetNodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkbatch.IBMS220SparkBatchS3NodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingFlumeNodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingKafkaAssemblyModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingKafkaAvroModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingKafkaClientModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingKinesisNodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingParquetNodeModuleGroup;
import org.talend.hadoop.distribution.ibms220.modulegroup.node.sparkstreaming.IBMS220SparkStreamingS3NodeModuleGroup;
import org.talend.hadoop.distribution.kafka.SparkStreamingKafkaVersion;
import org.talend.hadoop.distribution.spark.SparkClassPathUtils;

public class IBMS220Distribution extends AbstractDistribution implements
		HDFSComponent, SparkBatchComponent, SparkStreamingComponent {

	public static final String DISTRIBUTION_NAME = "IBMSPECTRUM";

	public static final String DISTRIBUTION_DISPLAY_NAME = "IBM Spectrum";

	public final static String VERSION = "IBMS_2_2_0";

	public static final String VERSION_DISPLAY = "IBM Spectrum 2.2 (Spark 2.1 only)";

	private final static String SPARK_MODULE_GROUP_NAME = "SPARK2-LIB-IBMS_2_2_0"; //$NON-NLS-1$

	protected Map<ComponentType, Set<DistributionModuleGroup>> moduleGroups;

	protected Map<NodeComponentTypeBean, Set<DistributionModuleGroup>> nodeModuleGroups;

	protected Map<ComponentType, ComponentCondition> displayConditions;

	public IBMS220Distribution() {
		displayConditions = buildDisplayConditions();
		moduleGroups = buildModuleGroups();
		nodeModuleGroups = buildNodeModuleGroups(getDistribution(),
				getVersion());
	}

	protected Map<ComponentType, ComponentCondition> buildDisplayConditions() {
		return new HashMap<>();
	}

	protected Map<ComponentType, Set<DistributionModuleGroup>> buildModuleGroups() {
		Map<ComponentType, Set<DistributionModuleGroup>> result = new HashMap<>();
		result.put(ComponentType.HDFS, IBMS220HDFSModuleGroup.getModuleGroups());
		result.put(ComponentType.SPARKBATCH,
				IBMS220SparkBatchModuleGroup.getModuleGroups());
		result.put(ComponentType.SPARKSTREAMING,
				IBMS220SparkStreamingModuleGroup.getModuleGroups());

		return result;
	}

	protected Map<NodeComponentTypeBean, Set<DistributionModuleGroup>> buildNodeModuleGroups(
			String distribution, String version) {
		Map<NodeComponentTypeBean, Set<DistributionModuleGroup>> result = new HashMap<>();
		// Spark Batch Parquet nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKBATCH,
				SparkBatchConstant.PARQUET_INPUT_COMPONENT),
				IBMS220SparkBatchParquetNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKBATCH,
				SparkBatchConstant.PARQUET_OUTPUT_COMPONENT),
				IBMS220SparkBatchParquetNodeModuleGroup.getModuleGroups(
						distribution, version));

		// Spark Batch S3 nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKBATCH,
				SparkBatchConstant.S3_CONFIGURATION_COMPONENT),
				IBMS220SparkBatchS3NodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKBATCH,
				SparkBatchConstant.MATCH_PREDICT_COMPONENT),
				IBMS220GraphFramesNodeModuleGroup.getModuleGroups(distribution,
						version));

		// Spark Streaming Parquet nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_INPUT_COMPONENT),
				IBMS220SparkStreamingParquetNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_OUTPUT_COMPONENT),
				IBMS220SparkStreamingParquetNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.PARQUET_STREAM_INPUT_COMPONENT),
				IBMS220SparkStreamingParquetNodeModuleGroup.getModuleGroups(
						distribution, version));

		// Spark Streaming S3 nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.S3_CONFIGURATION_COMPONENT),
				IBMS220SparkStreamingS3NodeModuleGroup.getModuleGroups(
						distribution, version));

		// Spark Streaming Kinesis nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_INPUT_COMPONENT),
				IBMS220SparkStreamingKinesisNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_INPUT_AVRO_COMPONENT),
				IBMS220SparkStreamingKinesisNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KINESIS_OUTPUT_COMPONENT),
				IBMS220SparkStreamingKinesisNodeModuleGroup.getModuleGroups(
						distribution, version));

		// Spark Streaming Kafka nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_INPUT_COMPONENT),
				IBMS220SparkStreamingKafkaAssemblyModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_AVRO_INPUT_COMPONENT),
				IBMS220SparkStreamingKafkaAvroModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.KAFKA_OUTPUT_COMPONENT),
				IBMS220SparkStreamingKafkaClientModuleGroup.getModuleGroups(
						distribution, version));

		// Spark Streaming Flume nodes
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.FLUME_INPUT_COMPONENT),
				IBMS220SparkStreamingFlumeNodeModuleGroup.getModuleGroups(
						distribution, version));
		result.put(new NodeComponentTypeBean(ComponentType.SPARKSTREAMING,
				SparkStreamingConstant.FLUME_OUTPUT_COMPONENT),
				IBMS220SparkStreamingFlumeNodeModuleGroup.getModuleGroups(
						distribution, version));

		return result;
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
	public String generateSparkJarsPaths(List<String> commandLineJarsPaths) {
		return SparkClassPathUtils.generateSparkJarsPaths(commandLineJarsPaths,
				SPARK_MODULE_GROUP_NAME);
	}

	@Override
	public boolean doSupportImpersonation() {
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
	public ESparkVersion getSparkVersion() {
		// TODO move spark versoin to 2.1
		return ESparkVersion.SPARK_2_0;
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
	public ComponentCondition getDisplayCondition(ComponentType componentType) {
		return displayConditions.get(componentType);
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
	public boolean doSupportS3() {
		return true;
	}

	@Override
	public boolean doSupportS3V4() {
		return true;
	}

	@Override
	public boolean doSupportParquetOutput() {
		return true;
	}

	@Override
	public boolean doSupportHDFSEncryption() {
		return true;
	}

	@Override
	public SparkStreamingKafkaVersion getSparkStreamingKafkaVersion() {
		return SparkStreamingKafkaVersion.KAFKA_0_10;
	}
}
