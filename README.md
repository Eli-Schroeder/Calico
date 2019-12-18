# Calico
Let's work together to make translation better.

# What is Calico?
Calico is my super ambitious attempt to beat Google translate. This repository does not contain the real guts of the program, but will instead be depended on by another program that passes in English Deep Structure trees. The purpose of this program is to transform the English deep structure trees into surface structure sentences of other languages.

# Wait wait wait... deep structure? Trees?
The deep structure is a language linguists came up with which is basically boiled-down language. It features every verbal unit as a clause (as opposed to absolute phrases, participial phrases, gerund phrases, and infinitive phrases), moves adverbial material, including adverb clauses, to behind the verb, and features no pronouns (for our purposes, we keep the pronouns in most of the time). These deep structure sentences can be diagrammed using a method refined by my good teacher Dr. Wink and his wife who recently passed away. Here's an example of a sentence converted to the deep structure and diagrammed as a tree diagram.

Surface Structure: The weather being nice, we went for a picnic in the park.

(N=we V=went <Prep=for Art=a N=picnic <Prep=in Art=the N=park>> (Art=the N=weather V=being Adj=nice))
```
    /NP
         \N --- we
\S
         /V --- went
    \VP
         \(AdvP)
                       /Prep --- for
                  \PP
                            /(Det)
                                    \Art --- a
                       \NP
                            \N --- picnic
                                   /Prep --- in
                            \(PP)
                                        /(Det)
                                                \Art --- the
                                   \NP
                                        \N --- park
         \(AdvP)
                           /(Det)
                                   \Art --- the
                      /NP
                           \N --- weather
                  \S
                           /V --- being
                      \VP
                           \(AdjP)
                                    \Adj --- nice
```

# Implications for translation

At first, I was convinced Spanish advanced grammar would look very different than English advanced grammar. But come to think of it, the primary differences are just syntax, and just as transformational rules can return the deep structure back to the English surface structure, perhaps the right T rules could return the deep structure to Spanish surface structure.

# Can I contribute?

Atsafruitly! Clone the repository and start writing your own T rules for English to Spanish translation. Your code will be a TRule object that takes a tree from the former TRule in the order of operation and modifies it to make it slightly more resemble a Spanish sentence. Please include plenty of comments to help us understand your genius code and make a PR. Also be sure to add it to the list of TRules in the array in "EnToSpanishRules.java" at whatever position would be most effective.
