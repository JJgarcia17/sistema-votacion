module.exports = {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}'
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f5f7ff',
          100: '#eef2ff',
          200: '#e0e7ff',
          300: '#c7d2fe',
          400: '#a5b4fc',
          500: '#6476f1',
          600: '#4f46e5',
          700: '#3730a3',
          800: '#27246f',
          900: '#0f1724'
        },
        accent: '#06b6d4',
        success: '#10b981',
        danger: '#ef4444'
      },
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', '-apple-system', 'Segoe UI', 'Roboto'],
        display: ['Poppins', 'Inter']
      },
      backgroundImage: {
        'header-gradient': 'linear-gradient(90deg,#eef2ff 0%, #f8fafc 100%)'
      },
      container: {
        center: true,
        padding: '1rem'
      }
    }
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
}
