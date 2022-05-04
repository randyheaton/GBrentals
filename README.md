# GBrentals
a demo of LazyColumn and ModalBottomSheeetLayout


This is a demo of how I would go about using LazyColumn and a ModalBottomSheetLayout for a mock media service. Did a few quirky things with how the query calls are structured because I wanted to experiment a little.


Here's a diagram:



```
/*
                        ┌────────────┐
                        │            │
                        │   Views    │
                        │            │
                        └┬─────────▲─┘
               notify    │         │ update state
                events   │         │   for
                 to      │         │
                       ┌─▼─────────┴───┐◄──────────────┐
                       │               │               │
                       │  ViewModel    ├────────┐      │
                       │               │        │      │
                       └───────────────┘        │      │ render JSON
                                       forward  │      │    for
                                       query    │      │
                                       through  │      │
                                                │      │
┌─────────────────────────────┐           ┌─────▼──────┴─────────┐
│                             │  provide  │   NetworkInterface   │
│                             │   key to  │   (data classes,     │
│  KeyProviderSingleInstance  ├───────────┼─►  ApiInterface,     │
│                             │           │    QueryGrammar)     │
│                             │           │                      │
│                             │           │                      │
└────┬─────────────────────▲──┘           └────┬─────────▲───────┘
     │                     │                   │         │
     │request key          │          request  │         │ provide JSON
     │   from              │           JSON    │         │    to
   ┌─▼──────────┐          │           from   ┌▼─────────┴───┐
   │            │          │                  │              │
   │            │          │                  │              │
   │  Resources ├──────────┘                  │   Server     │
   │            │   provide key               │              │
   │            │      to                     │              │
   └────────────┘                             └──────────────┘
 */
 ```
