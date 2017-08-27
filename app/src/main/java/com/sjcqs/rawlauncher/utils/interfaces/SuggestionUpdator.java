package com.sjcqs.rawlauncher.utils.interfaces;

import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionList;

import java.util.List;

/**
 * Created by satyan on 8/25/17.
 */

public interface SuggestionUpdator {

    SuggestionUpdate updateSuggestions(String input, List<Suggestion> current);

    class SuggestionUpdate{
        private SuggestionList toRemove;
        private SuggestionList toAdd;

        public SuggestionUpdate(SuggestionList toRemove, SuggestionList toAdd) {
            this.toRemove = toRemove;
            this.toAdd = toAdd;
        }

        public List<Suggestion> getToRemove() {
            return toRemove;
        }

        public List<Suggestion> getToAdd() {
            return toAdd;
        }
    }

}
