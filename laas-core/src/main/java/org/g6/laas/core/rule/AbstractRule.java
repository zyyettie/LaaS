package org.g6.laas.core.rule;


public abstract class AbstractRule implements Rule {

    public Rule and(Rule rule) {
        return new AndRule(this, rule);
    }

    public Rule or(Rule rule) {
        return new OrRule(this, rule);
    }

    public Rule not() {
        return new NotRule(this);
    }

    class NotRule extends AbstractRule {
        private Rule wrapper;

        public NotRule(Rule rule) {
            this.wrapper = rule;
        }

        public boolean isSatisfied(Object content) {
            return !wrapper.isSatisfied(content);
        }
    }

    class AndRule extends AbstractRule {
        private Rule one;
        private Rule other;

        public AndRule(Rule one, Rule other) {
            this.one = one;
            this.other = other;
        }

        public boolean isSatisfied(Object content) {
            return one.isSatisfied(content) && other.isSatisfied(content);
        }
    }

    class OrRule extends AbstractRule {
        private Rule one;
        private Rule other;

        public OrRule(Rule one, Rule other) {
            this.one = one;
            this.other = other;
        }

        public boolean isSatisfied(Object content) {
            return one.isSatisfied(content) || other.isSatisfied(content);
        }
    }
}
