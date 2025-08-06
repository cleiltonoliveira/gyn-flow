📦 com.seuapp.nome
├── 📁 core
│   ├── 📁 ui       → Temas, dimensões, componentes reutilizáveis (botões, inputs, etc.)
│   ├── 📁 util     → Classes utilitárias, extensões, helpers
│   ├── 📁 data     → Classes comuns de data (ex: Result<T>, exceptions)
│   └── 📁 di       → Configuração do Hilt, módulos de injeção
│
├── 📁 navigation
│   └── NavGraph.kt, Routes.kt
│
├── 📁 features
│   ├── 📁 login
│   │   ├── 📁 data
│   │   │   ├── 📁 repository
│   │   │   └── 📁 model
│   │   ├── 📁 domain
│   │   │   ├── 📁 usecase
│   │   │   └── 📁 model
│   │   └── 📁 presentation
│   │       ├── 📁 components → Subcomposables reutilizáveis
│   │       ├── 📁 screens    → Telas/Composables principais
│   │       ├── LoginViewModel.kt
│   │       └── LoginUiState.kt
│   │
│   ├── 📁 treino
│   │   └── ... (estrutura semelhante à de login)
│   └── 📁 dashboard
│       └── ...
│
├── 📁 app
│   ├── MainActivity.kt
│   └── App.kt


📦 com.example.treinofit
├── 📁 app
│   ├── App.kt
│   └── MainActivity.kt
│
├── 📁 core
│   ├── 📁 ui
│   │   ├── Theme.kt
│   │   ├── AppColors.kt
│   │   ├── AppTypography.kt
│   │   ├── Dimens.kt
│   │   └── components/
│   │       ├── DefaultButton.kt
│   │       └── LoadingIndicator.kt
│   ├── 📁 util
│   │   ├── Extensions.kt
│   │   └── Validator.kt
│   ├── 📁 data
│   │   └── Resource.kt
│   └── 📁 di
│       ├── AppModule.kt
│       └── NetworkModule.kt
│
├── 📁 navigation
│   ├── NavGraph.kt
│   └── Routes.kt
│
├── 📁 features
│   ├── 📁 login
│   │   ├── 📁 data
│   │   │   ├── 📁 model
│   │   │   │   └── LoginRequest.kt
│   │   │   └── 📁 repository
│   │   │       └── LoginRepositoryImpl.kt
│   │   ├── 📁 domain
│   │   │   ├── 📁 usecase
│   │   │   │   └── LoginUseCase.kt
│   │   │   └── 📁 model
│   │   │       └── User.kt
│   │   └── 📁 presentation
│   │       ├── 📁 screens
│   │       │   └── LoginScreen.kt
│   │       ├── 📁 components
│   │       │   └── LoginForm.kt
│   │       ├── LoginViewModel.kt
│   │       └── LoginUiState.kt
│
│   ├── 📁 treino
│   │   ├── 📁 data
│   │   │   ├── model/
│   │   │   └── repository/
│   │   ├── 📁 domain
│   │   │   ├── model/
│   │   │   └── usecase/
│   │   └── 📁 presentation
│   │       ├── screens/
│   │       ├── components/
│   │       ├── TreinoViewModel.kt
│   │       └── TreinoUiState.kt
