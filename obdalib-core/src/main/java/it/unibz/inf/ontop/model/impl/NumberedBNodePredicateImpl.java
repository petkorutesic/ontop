package it.unibz.inf.ontop.model.impl;

/*
 * #%L
 * ontop-obdalib-core
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import it.unibz.inf.ontop.model.BNodePredicate;

/**
 * A special Blank node predicate for internal uses.
 * It has additional slot id for storing a number inside, which will be used for BNode template generation.
 */
public class NumberedBNodePredicateImpl extends PredicateImpl implements BNodePredicate {

    // The name of the function that creates URI's in Quest
    public static final String QUEST_BNODE = "BNODE";

    private final int id;

    public NumberedBNodePredicateImpl(int num) {
        super(QUEST_BNODE, 0, null);
        this.id = num;
    }

    @Override
    public NumberedBNodePredicateImpl clone() {
        return this;
    }

    @Override
    public String toString() {
        return QUEST_BNODE + "[" + id + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberedBNodePredicateImpl)) {
            return false;
        }

        NumberedBNodePredicateImpl that = (NumberedBNodePredicateImpl) o;

        return this.getArity() == super.getArity() && id == that.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }
}
