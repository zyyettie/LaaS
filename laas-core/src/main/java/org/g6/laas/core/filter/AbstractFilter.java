package org.g6.laas.core.filter;

public abstract class AbstractFilter implements IFilter {

    public IFilter and(IFilter rule) {
        return new AndFilter(this, rule);
    }

    public IFilter or(IFilter rule) {
        return new OrFilter(this, rule);
    }

    public IFilter not() {
        return new NotFilter(this);
    }

    class NotFilter extends AbstractFilter {
        private IFilter wrapper;

        public NotFilter(IFilter filter) {
            this.wrapper = filter;
        }

        @Override
        public boolean isFiltered(Object content) {
            return !wrapper.isFiltered(content);
        }

        @Override
        public String toString() {
            return "not " + wrapper.toString();
        }
    }

    class AndFilter extends AbstractFilter {
        private IFilter one;
        private IFilter other;

        public AndFilter(IFilter one, IFilter other) {
            this.one = one;
            this.other = other;
        }

        @Override
        public boolean isFiltered(Object content) {
            return one.isFiltered(content) && other.isFiltered(content);
        }

        @Override
        public String toString() {
            return one.toString() + " and " + other.toString();
        }
    }

    class OrFilter extends AbstractFilter {
        private IFilter one;
        private IFilter other;

        public OrFilter(IFilter one, IFilter other) {
            this.one = one;
            this.other = other;
        }

        @Override
        public boolean isFiltered(Object content) {
            return one.isFiltered(content) || other.isFiltered(content);
        }

        @Override
        public String toString() {
            return one.toString() + " or " + other.toString();
        }
    }
}
