package com.hazelcast.sql.rules;

import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rel.logical.LogicalProject;

public class HazelcastProjectRule extends RelOptRule {
    public static final RelOptRule INSTANCE = new HazelcastProjectRule();

    private HazelcastProjectRule() {
        super(
            // TODO: Why Convention.NONE is used in Drill?
            RelOptRule.operand(LogicalProject.class, RelOptRule.any()),
            RelFactories.LOGICAL_BUILDER,
            "HazelcastProjectRule"
        );
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        Project project = call.rel(0);

        RelNode input = project.getInput();

        RelTraitSet traits = project.getTraitSet().plus(HazelcastRel.LOGICAL);

        RelNode convertedInput = convert(input, input.getTraitSet().plus(HazelcastRel.LOGICAL).simplify());

        call.transformTo(new HazelcastProjectRel(
            project.getCluster(),
            traits,
            convertedInput,
            project.getProjects(),
            project.getRowType())
        );
    }
}
