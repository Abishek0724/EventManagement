import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  base: './', // 👈 important for relative asset paths on Vercel
  plugins: [react(), tailwindcss()],
  build: {
    outDir: 'dist'
  },
  server: {
    port: 3000
  }
})
